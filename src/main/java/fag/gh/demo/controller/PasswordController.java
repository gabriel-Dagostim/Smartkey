package fag.gh.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fag.gh.demo.model.PasswordModel;
import fag.gh.demo.model.UpdatePasswordModel;
import fag.gh.demo.model.UserModel;
import fag.gh.demo.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/senhas")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserService userService;

    @GetMapping("/senhasuser")
    public ResponseEntity<?> listarSenhasUsuario(@RequestHeader String idUser, @RequestHeader String token)
            throws Exception {
        try {

            UserModel user = userService.findByGuidId(idUser);
            VerifySession.verifyToken(token, user.getKey());
            return ResponseEntity.status(HttpStatus.OK).body(passwordService.findUserPasswords(idUser, user));

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<?> cadastrarSenha(@RequestHeader String idUser, @RequestBody PasswordModel senha,
            @RequestHeader String token)
            throws Exception {
        try {
            String chaveUser = userService.findByGuidId(idUser).getKey();
            VerifySession.verifyToken(token, chaveUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(passwordService.insert(senha, token, idUser));

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/senha")
    public ResponseEntity<?> buscarSenhasPorId(@RequestHeader String idUser, @RequestHeader String idSenha,
            @RequestHeader String token) {
        try {
            UserModel user = userService.findByGuidId(idUser);
            VerifySession.verifyToken(token, user.getKey());

            return ResponseEntity.status(HttpStatus.OK).body(passwordService.findByGuidId(user, idSenha));

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @DeleteMapping
    public ResponseEntity<?> deletarSenha(@RequestHeader String idUser, @RequestHeader String idSenha,
            @RequestHeader String token) throws Exception {
        try {
            String chaveUser = userService.findByGuidId(idUser).getKey();
            VerifySession.verifyToken(token, chaveUser);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(passwordService.deletePassword(idSenha, token));

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> AlterarSenha(@RequestHeader String idUser, @RequestHeader String idSenha,
            @RequestBody UpdatePasswordModel data,
            @RequestHeader String token)
            throws Exception {
        try {
            String chaveUser = userService.findByGuidId(idUser).getKey();
            VerifySession.verifyToken(token, chaveUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(passwordService.updatePassword(idSenha, idUser, data));

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/qntSenhas")
    public ResponseEntity<?> buscarQntSenhas(@RequestHeader String idUser, @RequestHeader String token) {
        try {
            UserModel user = userService.findByGuidId(idUser);
            VerifySession.verifyToken(token, user.getKey());

            List<PasswordModel> senhas = passwordService.findUserPasswords(idUser, user);
            int qntSenhas = senhas.size();

            return ResponseEntity.status(HttpStatus.OK).body(qntSenhas);
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }
}
