/**
 * example.demo 予約ドメイン
 *              第三者に影響を及ばさないことが保証されている
 *              適当なドメインを使用した場合は、メールの誤配信や、ドメイン先が保証されない
 */

package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.model.SiteUser;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.util.Authority;

import jakarta.transaction.Transactional;

/**
 * クラスにpublicがない
 * @SpringBootTest Spring Bootの機能を有効にする
 * @Transactional テスト開始から終了までを、トランザクション処理にする
 *                  これを付与すると、各テスト後にデータが初期化(ロールバック)される
 */
@SpringBootTest
@Transactional
class UserDetailsServiceImplTest {
	
	@Autowired
	SiteUserRepository repository;
	
	@Autowired
	UserDetailsServiceImpl service;

	/**
	 * @Test テストメソッドであることを示す
	 * @DisplayName 表示名(テスト名)を付けることができる
	 */
	@Test
	@DisplayName("ユーザ名が存在する場合、ユーザ詳細を取得することを期待します。")
	void whenUsernameExists_expectToGetUserDetails() {
	
		/* AAA(Arrange-Act-Assert)パターン */
		
		// Arrange(準備する)
		var user = new SiteUser();
		user.setUsername("Harada");
		user.setPassword("password");
		user.setEmail("harada@example.com");
		user.setAuthority(Authority.USER);
		repository.save(user);
		
		// Act(実行する)
		var actual = service.loadUserByUsername("Harada");
		
		// Assert(検証する)
		/**
		 * assertEquals(期待する値、実際の値)
		 */
		assertEquals(user.getUsername(), actual.getUsername());
	}
	
	@Test
	@DisplayName("ユーザ名が存在しない時、例外をスローします")
	void whenUsernameDoesNotExist_thorowException() {
		// Act & Assert
		// 例外をスローするかの検証は、assertThrowsを使用する
		assertThrows(UsernameNotFoundException.class,
				() -> service.loadUserByUsername("Takeda"));
	}

}
