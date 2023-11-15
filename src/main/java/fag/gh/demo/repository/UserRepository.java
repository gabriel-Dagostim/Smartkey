package fag.gh.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fag.gh.demo.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, String>, JpaSpecificationExecutor<UserModel> {
    public final static String BUSCAR_POR_EMAIL = "SELECT * FROM TB_USER WHERE EMAIL LIKE :email";
    public final static String FIND_BY_ID = "SELECT * FROM TB_USER WHERE ID_USER LIKE :idUser";
    public final static String DELETE_BY_ID = "DELETE FROM TB_USER WHERE ID_USER LIKE :idUser";

    @Query(value = BUSCAR_POR_EMAIL, nativeQuery = true)
    public UserModel findByEmail(@Param("email") final String email);

    @Query(value = FIND_BY_ID, nativeQuery = true)
    public UserModel findByGuidId(@Param("idUser") final String idUser);

    @Query(value = DELETE_BY_ID, nativeQuery = true)
    public void deleteByGuidId(@Param("idUser") final String idUser);
}
