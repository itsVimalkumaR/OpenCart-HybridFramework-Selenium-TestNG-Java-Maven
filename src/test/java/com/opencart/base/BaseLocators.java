/* /src/test/java/com/opencart/base/BaseLocators.java */
package com.opencart.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * BaseLocators - common locators shared across multiple pages
 */
public class BaseLocators {

	protected WebDriver driver;

	/** Constructor */
	public BaseLocators(WebDriver driver) {
		this.driver = driver;
	}

	/* ---------------- Header / Navigation ----------- */
	public final By HOME_LINK = By.linkText("Your Store");
	public final By MY_ACCOUNT_DROPDOWN = By.xpath("//span[text()='My Account']");
	public final By REGISTER_LINK = By.xpath("//a[text()='Register']");
	public final By PAGE_HEADING = By.xpath("//h1[contains(text(),'Register Account')]");
    public final By BREADCRUMB = By.className("breadcrumb");
	public final By LOGOUT_LINK = By.xpath("//a[text()='Logout']");
	public final By LOGIN_LINK = By.xpath("//a[text()='Login']");

	/* ---------- Register Page Fields --------- */
	public final By FIRST_NAME_INPUT_FIELD = By.id("input-firstname");
	public final By LAST_NAME_INPUT_FIELD = By.id("input-lastname");
	public final By EMAIL_INPUT_FIELD = By.id("input-email");
	public final By TELEPHONE_INPUT_FIELD = By.id("input-telephone");
	public final By PASSWORD_INPUT_FIELD = By.id("input-password");
	public final By CONFIRM_PASSWORD_INPUT_FIELD = By.id("input-confirm");
	
	public final By NEWSLETTER_YES = By.xpath("//input[@name='newsletter' and @value='1']");
	public final By NEWSLATTER_NO = By.xpath("//input[@name='newsletter' and @value='0']");
	public final By PRIVACY_POLICY_CHECKBOX = By.name("agree");
	public final By CONTINUE_LINK_BUTTON = By.xpath("//div[@class='pull-right']//a[.='Continue']");
	public final By CONTINUE_BUTTON = By.xpath("//*[@value='Continue']");

	/* --------------- Error Messages & Alerts ----------- */
	public final By SUCCESS_MESSAGE = By.xpath("//div[@id='content']//p[contains(text(), 'Congratulations!')]");
	public final By WARNING_ALERT = By.cssSelector(".alert.alert-danger");
	public final By SUCCESS_ALERT = By.cssSelector(".alert.alert-success");
	public final By INFO_ALERT = By.cssSelector(".alert.alert-info");

	// Field-specific error messages
	public final By FIRST_NAME_ERROR = By.xpath("//input[@id='input-firstname']/following-sibling::div");
	public final By LAST_NAME_ERROR = By.xpath("//input[@id='input-lastname']/following-sibling::div");
	public final By EMAIL_ERROR = By.xpath("//input[@id='input-email']/following-sibling::div");
	public final By TELEPHONE_ERROR = By.xpath("//input[@id='input-telephone']/following-sibling::div");
	public final By PASSWORD_ERROR = By.xpath("//input[@id='input-password']/following-sibling::div");
	public final By CONFIRM_PASSWORD_ERROR = By.xpath("//input[@id='input-confirm']/following-sibling::div");

	// Specific error messages
	public final By FIRST_NAME_EMPTY_ERROR = By.xpath("//div[contains(text(), 'First Name must be between')]");
	public final By LAST_NAME_EMPTY_ERROR = By.xpath("//div[contains(text(), 'Last Name must be between')]");
	public final By EMAIL_INVALID_ERROR = By
			.xpath("//div[contains(text(), 'E-Mail Address does not appear to be valid')]");
	public final By TELEPHONE_INVALID_ERROR = By.xpath("//div[contains(text(), 'Telephone must be between')]");
	public final By PASSWORD_LENGTH_ERROR = By.xpath("//div[contains(text(), 'Password must be between')]");
	public final By PASSWORD_MISMATCH_ERROR = By
			.xpath("//div[contains(text(), 'Password confirmation does not match password')]");
	public final By EMAIL_EXISTS_ERROR = By.xpath("//div[contains(text(), 'E-Mail Address is already registered')]");
	public final By PRIVACY_POLICY_ERROR = By
			.xpath("//div[contains(text(), 'Warning: You must agree to the Privacy Policy')]");

	/*  ---------------- Common UI Elements ------------ */
	public final By SUBMIT_BUTTON = By.xpath("//input[@type='submit']");
	public final By BACK_BUTTON = By.xpath("//a[contains(@class, 'btn') and contains(text(), 'Back')]");
	public final By FOOTER = By.tagName("footer");
	public final By HOME_ICON = By.xpath("//i[contains(@class,'fa-home')]");

	/* ---------------- Placeholder locators (for validation) ------------- */
	public final By FIRST_NAME_PLACEHOLDER = By.id("input-firstname");
	public final By LAST_NAME_PLACEHOLDER = By.id("input-lastname");
	public final By EMAIL_PLACEHOLDER = By.id("input-email");
	public final By TELEPHONE_PLACEHOLDER = By.id("input-telephone");
	public final By PASSWORD_PLACEHOLDER = By.id("input-password");
	public final By CONFIRM_PASSWORD_PLACEHOLDER = By.id("input-confirm");
}