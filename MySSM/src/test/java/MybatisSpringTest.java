import com.haiking.ssm.pojo.Account;
import com.haiking.ssm.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(locations = {"classpath*:application*.xml"})
public class MybatisSpringTest {

    @Autowired
    private AccountService accountService;
    @Test
    public void test1(){
        try{
        List<Account> accountList = accountService.queryAccountList();
        for(Account account :accountList){
            System.out.println(account);
        }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
