import com.haiking.ssm.dao.ResumeDao;
import com.haiking.ssm.pojo.Resume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ResumeTest {

    @Autowired
    private ResumeDao resumeDao;

    @Test
    public void findById() {
        Resume resume = new Resume();
        resume.setId(1L);
        Optional<Resume> resumeDaoById = resumeDao.findById(1L);
        Resume resume1 = resumeDaoById.get();
        System.out.println(resume1);
    }

}
