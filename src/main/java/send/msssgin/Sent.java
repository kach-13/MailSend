package send.msssgin;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@RestController
@RequestMapping("send/")
public class Sent {
    @GetMapping("/messgin")
        public  void sendEmail() throws AddressException, MessagingException, IOException {
        Properties prop = new Properties();//配置文件存邮件信息
        prop.load(new InputStreamReader(Sent.class.getClassLoader().getResourceAsStream("application.properties"), "UTF-8"));
        String host = prop.getProperty("mail.smtp.host");
        String auth = prop.getProperty("mail.smtp.auth");
        String protocol = prop.getProperty("mail.smtp.protocol");
        String port = prop.getProperty("mail.smtp.socketFactory.port");
        String user = prop.getProperty("mail.user");
        String auth_code = prop.getProperty("mail.auth_code");
        String subject = prop.getProperty("mail.subject");
        String sender = prop.getProperty("mail.sender");
        String addressee = prop.getProperty("mail.addressee");
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");// 连接协议
            properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
            properties.put("mail.smtp.port", 465);// 端口号
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
            properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");//重要
            // 得到回话对象
            Session session = Session.getInstance(properties);
            // 获取邮件对象
            Message message = new MimeMessage(session);
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(sender));
            // 设置收件人邮箱地址
            //String email = "1097723239@qq.com";
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(addressee)});
            //message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));//一个收件人
            // 设置邮件标题
            message.setSubject(subject);
            // 设置邮件内容
            //需要附带图片时携带
        //一个Multipart对象包含一个或多个bodypart对象，组成邮件正文
        MimeMultipart multipart = new MimeMultipart();
        //读取本地图片,将图片数据添加到"节点"
        MimeBodyPart image = new MimeBodyPart();                        //图片地址
        DataHandler dataHandler1 = new DataHandler(new FileDataSource("C:\\Users\\Hasee\\Pictures\\Saved Pictures\\xiao.jpg"));
        image.setDataHandler(dataHandler1);
        image.setContentID("image");
        //创建文本节点
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("消息","text/html;charset=UTF-8");
        //将文本和图片添加到multipart
        multipart.addBodyPart(text);
        multipart.addBodyPart(image);//不携图片带把这条注释掉
        multipart.setSubType("related");

            //message.setText("压力马斯内");//这是我们的邮件要发送的信息内容
            message.setContent(multipart);//这是我们的邮件要发送的信息内容
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            transport.connect(sender, auth_code);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码,输入自己的即可
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
    }



