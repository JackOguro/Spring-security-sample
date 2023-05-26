package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.SiteUser;
import com.example.demo.util.Authority;

import jakarta.transaction.Transactional;

/**
 * @SpringBootTest Spring Bootの機能を有効にする
 * @AutoConfigureMockMvc MockMvcの自動構成を有効にします。
 *                         MockMvcを使用する場合は、@SpringBootTestなどと一緒に使用します
 * @Transactional テスト開始から終了までを、トランザクション処理にする
 *                  これを付与すると、各テスト後にデータが初期化(ロールバック)される
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityControllerTest {
	
	/**
	 * mockMvc Spring MVCのテストができるクラス
	 */
	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("登録エラーある時、エラーを表示することを期待します")
	void whenThereIsRegistrationError_expectToSeeErrors() throws Exception {
		mockMvc
			// リクエストを実行します
			.perform(
				// HTTPのPOSTリクエストを使用します
				// HTTPの場合 post("/register")
				// HTTPSの場合 post("/register").secure(true)
				post("/register")
				// 入力の属性を設定します
				// Controller側の@ModelAttributeに対して値を設定できます
				.flashAttr("user", new SiteUser())
				// CSRFトークンを自動挿入します。POSTリクエストには必要
				.with(csrf())
			)
			// andExpect 期待する内容(結果)を指定する
			// エラーがあることを検証します
			.andExpect(model().hasErrors())
			// 指定したHTMLを表示しているか検証します
			.andExpect(view().name("register"));
			
	}
	
	@Test
	@DisplayName("管理者ユーザとして登録する時、成功することを期待します")
	void whenRegisteringAsAdminUser_expaectToSucceed() throws Exception {
		var user = new SiteUser();
		user.setUsername("管理者ユーザ");
		user.setPassword("password");
		user.setEmail("admin@example.com");
		user.setGender(0);
		user.setAdmin(true);
		user.setAuthority(Authority.ADMIN);
		mockMvc.perform(post("/register")
			.flashAttr("user", user).with(csrf()))
			// エラーがないことを検証します
			.andExpect(model().hasNoErrors())
			// 指定したURLに、リダイレクトすることを検証します
			.andExpect(redirectedUrl("/login?register"))
			// ステータスコードが、Found(302=リダイレクト)であることを検証します
			.andExpect(status().isFound());
	}
	
	/**
	 * @WithMockUser モックユーザーでログインします、
	 *                username モックユーザーの名前を指定する
	 *                authorities モックユーザの権限を指定する 配列で複数指定も可能
	 * @WithMockUser ユーザ指定無し
	 * @WithMockUser(username="admin", authorities="ADMIN") ユーザと権限を指定
	 * @WithMockUser(username="admin", authorities={"USER, "ADMIN"})
	 */
	@Test
	@DisplayName("管理者ユーザーでログイン時、ユーザ一覧を表示することを期待します")
	@WithMockUser(username="admin", authorities="ADMIN")
	void whenLoggedInAsAdminUser_expectToSeeListOfUsers() throws Exception {
		mockMvc.perform(get("/admin/list"))
		// ステータスコードが、OK(200)であることを検証します
		.andExpect(status().isOk())
		// HTMLの表示内容に、指定した文字列を含んでいるか検証します
		.andExpect(content().string(containsString("ユーザー一覧")))
		.andExpect(view().name("list"));
	}

}
