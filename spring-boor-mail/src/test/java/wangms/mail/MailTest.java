import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import wangms.mail.MailApplication;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送测试类
 *
 * @author: wangms
 * @date: 2021-09-09 17:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailApplication.class)
public class MailTest {

    @Autowired
    private TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 包含模版附件测试
     */
    @Test
    public void sendTemplateMail() {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);// 发送人的邮箱
            messageHelper.setTo("11111@qq.com");//发给谁  对方邮箱
            messageHelper.setSubject("包含模版附件测试"); //标题
            //使用模板thymeleaf
            //Context是导这个包import org.thymeleaf.context.Context;
            Context context = new Context();
            //定义模板数据
            Map<String, Object> map = new HashMap<>();
            map.put("msg", "模版变量填充");
            map.put("name", "张三");
            map.put("mobile", "13355667711");
            context.setVariables(map);
            //获取thymeleaf的html模板
            String emailContent = templateEngine.process("/mail/demo", context); //指定模板路径
            messageHelper.setText(emailContent, true);
            //附件
            List<Pair<String, File>> attachments = new ArrayList<>();
            String filename = "C:\\Users\\Administrator\\Desktop\\图片\\123.jpg";
            File file = new File(filename);
            Pair<String, File> pair = new Pair<>("123.jpg", file);
            attachments.add(pair);

            if (attachments != null) {
                for (Pair<String, File> attachment : attachments) {
                    FileSystemResource file1 = new FileSystemResource(attachment.getValue());
                    messageHelper.addAttachment(attachment.getKey(), file1);
                }
            }
            //发送邮件
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
