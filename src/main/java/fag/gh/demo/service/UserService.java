package fag.gh.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fag.gh.demo.model.PasswordModel;
import fag.gh.demo.model.UpdateUserModel;
import fag.gh.demo.model.UserModel;
import fag.gh.demo.repository.UserRepository;
import fag.gh.demo.tools.DateStringConverter;
import fag.gh.demo.tools.EncryptionUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private PasswordService passwordService;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public UserModel findByGuidId(String idUser) {

        UserModel obj = userRepository.findByGuidId(idUser);

        if (obj == null)
            throw new NullPointerException("Usuario inexistente");

        return obj;
    }

    public UserModel insert(UserModel obj) throws Exception {
        UserModel userExiste = findByEmail(obj.getEmail());
        if (userExiste != null) {
            throw new Exception("Email já cadastrado na base de dados");
        }

        String upperEmail = obj.getEmail().toUpperCase();
        String upperNome = obj.getNome().toUpperCase();
        String enconder = this.passwordEncoder.encode(obj.getSenha());
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        String generatekey = EncryptionUtils.generateRdnToken();

        obj.setEmail(upperEmail);
        obj.setNome(upperNome);
        obj.setTelefone(obj.getTelefone());
        obj.setSenha(enconder);
        obj.setDataCriacao(dataHoraAtual);
        obj.setKey(generatekey);

        return userRepository.save(obj);
    }

    public UserModel findByEmail(String email) throws Exception {
        String emailUpper = email.toUpperCase();
        UserModel user = userRepository.findByEmail(emailUpper);
        return user;
    }

    public LoginModel login(String email, String senha) throws Exception {
        UserModel user = userRepository.findByEmail(email.toUpperCase());
        if (user == null)
            throw new NullPointerException("Usuario inexistente");

        LocalDateTime dataHoraAtual = LocalDateTime.now();
        String dataString = DateStringConverter.convert(dataHoraAtual);
        String token = EncryptionUtils.encryptData(dataString, user.getKey());

        String senhaUser = user.getSenha();
        boolean isValido = passwordEncoder.matches(senha, senhaUser);

        LoginModel infoLogin = new LoginModel();

        infoLogin.setIdUser(user.getId());
        infoLogin.setToken(token);
        infoLogin.setValido(isValido);

        return infoLogin;
    }

    public UserModel update(String id, UpdateUserModel user, String token) throws Exception {
        try {
            UserModel entity = userRepository.findByGuidId(id);
            if (entity == null)
                throw new NullPointerException("Usuario inexistente");

            updateData(entity, user);
            return userRepository.save(entity);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    private void updateData(UserModel entity, UpdateUserModel obj) {
        LocalDateTime dataAtual = LocalDateTime.now();

        if (!(obj.getNome().isEmpty() || obj.getNome().isBlank()))
            entity.setNome(obj.getNome().toUpperCase());

        if (!(obj.getEmail().isEmpty() || obj.getEmail().isBlank()))
            entity.setEmail(obj.getEmail().toUpperCase());

        if (!(obj.getTelefone().isEmpty() || obj.getTelefone().isBlank()))
            entity.setTelefone(obj.getTelefone());

        if (!(obj.getSenha().isEmpty() || obj.getSenha().isBlank()))
            entity.setSenha(obj.getSenha());

        entity.setDataAlteracao(dataAtual);

    }

    public UserModel deleteByGuidId(String idUser, String token) throws Exception {

        UserModel userDeletado = userRepository.findByGuidId(idUser);
        List<PasswordModel> listaSenhas = passwordService.findUserPasswords(idUser, userDeletado);

        for (PasswordModel passwordModel : listaSenhas) {

            passwordService.deleteById(passwordModel.getIdSenha(), idUser, token);
        }

        if (userDeletado == null)
            throw new NullPointerException("Usuario inexistente");

        try {
            userRepository.deleteById(idUser);
        } catch (Exception e) {
            throw new Exception("Algo deu errado ao deletar usuario: " + e.getMessage());
        }

        return userDeletado;
    }
}
