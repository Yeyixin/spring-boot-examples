package com.neo.repository;

import com.neo.model.Address;
import com.neo.model.UserDetail;
import com.neo.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailRepositoryTests {

	@Resource
	private AddressRepository addressRepository;
	@Resource
	private UserDetailRepository userDetailRepository;

	@Test
	public void testSaveAddress() {
		Address address=new Address();
		address.setUserId(1L);
		address.setCity("北京");
		address.setProvince("北京");
		address.setStreet("分钟寺");
		addressRepository.save(address);
	}

	@Test
	public void testSaveUserDetail() {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		String formattedDate = dateFormat.format(date);
		UserDetail userDetail=new UserDetail("123", 28, "小明", UserDetail.Status.ACTIVE, "钓鱼", "一个爱玩的人", "127.0.0.1");
		userDetailRepository.save(userDetail);
	}

	@Test
	public void testUserInfo()  {
		List<UserInfo> userInfos=userDetailRepository.findUserInfo("钓鱼");
		for (UserInfo userInfo:userInfos){
			System.out.println("userInfo: "+userInfo.getUserName()+"-"+userInfo.getEmail()+"-"+userInfo.getHobby()+"-"+userInfo.getIntroduction());
		}
	}
}