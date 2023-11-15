package fag.gh.demo.controller;

import java.util.List;

import javax.validation.Valid;

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

import fag.gh.demo.model.UpdateUserModel;
import fag.gh.demo.model.UserModel;
import fag.gh.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@Api(value = "Usuarios")
@RequestMapping(value = "/usuarios")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation(value = "Lista de usuarios")
    @GetMapping
    public ResponseEntity<List<UserModel>> buscarTodosUsuarios() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/userid")
    public ResponseEntity<?> buscarUserPorId(@RequestHeader String idUser, @RequestHeader String token)
            throws Exception {
        try {
            UserModel user = service.findByGuidId(idUser);
            VerifySession.verifyToken(token, user.getKey());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UserModel user) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/usuario")
    public ResponseEntity<?> buscarUsuarioPorEmail(@RequestHeader String email, @RequestHeader String token) {
        try {

            UserModel user = service.findByEmail(email);
            VerifySession.verifyToken(token, user.getKey());
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping
    public ResponseEntity<?> AlterarUser(@RequestHeader String idUser, @RequestBody UpdateUserModel data,
            @RequestHeader String token)
            throws Exception {
        try {
            String chaveUser = service.findByGuidId(idUser).getKey();
            VerifySession.verifyToken(token, chaveUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(service.update(idUser, data, token));

        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> validarLogin(@RequestHeader String usuario, @RequestHeader String senha)
            throws Exception {
        try {
            LoginModel responseLogin = service.login(usuario, senha);

            if (!responseLogin.isValido())
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseLogin);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @DeleteMapping
    public ResponseEntity<?> deletarUser(@RequestHeader String idUser, @RequestHeader String token) throws Exception {
        try {

            String chaveUser = service.findByGuidId(idUser).getKey();
            VerifySession.verifyToken(token, chaveUser);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.deleteByGuidId(idUser, token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
