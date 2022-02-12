import com.ray.server.service.IAdminService;
import com.ray.server.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = {YebApplicationTest.class})
public class YebApplicationTest {
    @Autowired
    private IAdminService adminService;

    @Test
    public void test(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        System.out.println(encode);
        System.out.println(encoder.matches(encode, adminService.getAdminByUserName("admin").getPassword()));
    }
}
