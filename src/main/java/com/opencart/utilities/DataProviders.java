/* /src/main/java/com/opencart/utilities/DataProviders.java */
package com.opencart.utilities;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

public class DataProviders {

	// Use Log4j Logger instead of java.lang.System.Logger
	public static Logger logger = Logger.getLogger(DataProviders.class);

	@DataProvider(name = "RegistrationData")
	public Object[][] getRegistrationData() {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";
		String sheetName = "Data";

		ExcelUtils excel = new ExcelUtils(filePath, sheetName);
		Object[][] data = excel.getSheetDataAs2DArray();
		excel.close();

		// Log the data being used
		logger.info("[INFO] Loading " + data.length + " test cases from Excel:");
		for (int i = 0; i < data.length; i++) {
			logger.info("Test Case " + (i + 1) + ": " + java.util.Arrays.toString(data[i]));
		}

		return data;
	}

	@DataProvider(name = "ValidRegistrationData")
	public Object[][] getValidRegistrationData() {
		Object[][] allData = getRegistrationData();
		java.util.List<Object[]> validData = new java.util.ArrayList<>();

		for (Object[] row : allData) {
			if (isValidRegistrationData(row)) {
				validData.add(row);
			}
		}

		return validData.toArray(new Object[0][]);
	}

	@DataProvider(name = "InvalidRegistrationData")
	public Object[][] getInvalidRegistrationData() {
		Object[][] allData = getRegistrationData();
		java.util.List<Object[]> invalidData = new java.util.ArrayList<>();

		for (Object[] row : allData) {
			if (!isValidRegistrationData(row)) {
				invalidData.add(row);
			}
		}

		return invalidData.toArray(new Object[0][]);
	}

	@DataProvider(name = "PrivacyPolicyData")
	public Object[][] getPrivacyPolicyData() {
		Object[][] allData = getRegistrationData();
		java.util.List<Object[]> privacyPolicyData = new java.util.ArrayList<>();

		for (Object[] row : allData) {
			String privacyPolicy = row[6].toString(); // 7th column (index 6)
			if ("FALSE".equalsIgnoreCase(privacyPolicy)) {
				privacyPolicyData.add(row);
			}
		}

		return privacyPolicyData.toArray(new Object[0][]);
	}

	@DataProvider(name = "PasswordMismatchData")
	public Object[][] getPasswordMismatchData() {
		Object[][] allData = getRegistrationData();
		java.util.List<Object[]> mismatchData = new java.util.ArrayList<>();

		for (Object[] row : allData) {
			String password = row[4].toString(); // 5th column
			String confirmPassword = row[5].toString(); // 6th column
			if (!password.equals(confirmPassword)) {
				mismatchData.add(row);
			}
		}

		return mismatchData.toArray(new Object[0][]);
	}

	// LOGIN DATA PROVIDERS

	@DataProvider(name = "ValidLoginData")
	public Object[][] getValidLoginData() {
		Object[][] allData = getRegistrationData();
		java.util.List<Object[]> validLoginData = new java.util.ArrayList<>();

		for (Object[] row : allData) {
			// FIXED: Use correct column index - Email is at index 2, Password at index 4
			String email = row[2].toString(); // Email column (index 2)
			String password = row[4].toString(); // Password column (index 4)

			// Log using proper Log4j syntax
			logger.info("Processing email: " + email + ", Password: " + password);

			// Valid login data: non-empty email and password with null checks
			if (email != null && !email.isEmpty() && password != null && !password.isEmpty() && isValidEmail(email)) {
				validLoginData.add(new Object[] { email, password });
				logger.info("Added valid login: " + email);
			} else {
				logger.warn("Skipping invalid login data - Email: " + email + ", Valid: " + isValidEmail(email));
			}
		}

		// Add the specific test email from your requirements
		validLoginData.add(new Object[] { "vk636485@gmail.com", "Vimalutr16@" });
//        validLoginData.add(new Object[] { "vimalkumarm523@gmail.com", "Tester123!" });

		logger.info("[INFO] Found " + validLoginData.size() + " valid login test cases");
		return validLoginData.toArray(new Object[0][]);
	}

	@DataProvider(name = "InvalidLoginData")
	public Object[][] getInvalidLoginData() {
		java.util.List<Object[]> invalidLoginData = new java.util.ArrayList<>();

		// Add various invalid login scenarios
		invalidLoginData.add(new Object[] { "invalid@test.com", "WrongPassword123!" });
		invalidLoginData.add(new Object[] { "", "Password@123" });
		invalidLoginData.add(new Object[] { "test@test.com", "" });
		invalidLoginData.add(new Object[] { "", "" });
		invalidLoginData.add(new Object[] { "invalidemail", "Password@123" });
		invalidLoginData.add(new Object[] { "vimalkumarm523@gmail.com", "WrongPassword" });

		logger.info("[INFO] Created " + invalidLoginData.size() + " invalid login test cases");
		return invalidLoginData.toArray(new Object[0][]);
	}

	@DataProvider(name = "LoginData")
	public Object[][] getLoginData() {
		java.util.List<Object[]> loginData = new java.util.ArrayList<>();

		// Valid login data
		loginData.add(new Object[] { "vimalkumarm523@gmail.com", "Tester123!", "SUCCESS" });
		loginData.add(new Object[] { "vk636485@gmail.com", "Vimalutr16@", "SUCCESS" });

		// Get valid emails from registration data
		Object[][] regData = getRegistrationData();
		for (Object[] row : regData) {
			String email = row[2].toString(); // Email column
			String password = row[4].toString(); // Password column
			if (email != null && !email.isEmpty() && password != null && !password.isEmpty() && isValidEmail(email)) {
				loginData.add(new Object[] { email, password, "SUCCESS" });
			}
		}

		// Invalid login data
		loginData.add(new Object[] { "invalid@test.com", "WrongPassword123!", "FAILURE" });
		loginData.add(new Object[] { "", "Password@123", "FAILURE" });
		loginData.add(new Object[] { "test@test.com", "", "FAILURE" });
		loginData.add(new Object[] { "", "", "FAILURE" });
		loginData.add(new Object[] { "invalidemail", "Password@123", "FAILURE" });

		logger.info("[INFO] Created " + loginData.size() + " comprehensive login test cases");
		return loginData.toArray(new Object[0][]);
	}

	@DataProvider(name = "LoginCredentials")
	public Object[][] getLoginCredentials() {
		return new Object[][] { { "vimalkumarm523@gmail.com", "Tester123!", true }, // Valid credentials
				{ "vk636485@gmail.com", "Vimalutr16@", true }, // Valid credentials
				{ "invalid@test.com", "WrongPassword123!", false }, // Invalid both
				{ "invalid@test.com", "Tester123!", false }, // Invalid email
				{ "vimalkumarm523@gmail.com", "WrongPassword", false }, // Invalid password
				{ "", "", false } // Empty credentials
		};
	}

	private boolean isValidRegistrationData(Object[] row) {
		if (row.length < 7)
			return false;

		String firstName = row[0].toString();
		String lastName = row[1].toString();
		String email = row[2].toString();
		String phone = row[3].toString();
		String password = row[4].toString();
		String confirmPassword = row[5].toString();
		String privacyPolicy = row[6].toString();

		return !firstName.isEmpty() && !lastName.isEmpty() && isValidEmail(email) && isValidPhone(phone)
				&& !password.isEmpty() && password.equals(confirmPassword) && "TRUE".equalsIgnoreCase(privacyPolicy);
	}

	@DataProvider(name = "LastRowLoginData")
	public Object[][] getLastRowLoginData() {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";
		String sheetName = "Data";

		ExcelUtils excel = new ExcelUtils(filePath, sheetName);

		// Get the last row index
		int totalRows = excel.getRowCount();
		logger.info("Total rows in Excel: " + totalRows);

		if (totalRows > 1) { // Assuming row 0 is header
			int lastRowIndex = totalRows - 1;

			// Get data from last row
			String email = excel.getCellData(lastRowIndex, 2); // Column 2 - Email
			String password = excel.getCellData(lastRowIndex, 4); // Column 4 - Password

			logger.info("Last row data - Email: " + email + ", Password: " + password);

			excel.close();

			if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
				return new Object[][] { { email, password } };
			}
		}

		excel.close();

		// Fallback to default credentials
		logger.warn("Using default credentials as last row data is empty");
		return new Object[][] { { "vk636485@gmail.com", "Vimalutr16@" } };
	}

	private boolean isValidEmail(String email) {
		return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
	}

	private boolean isValidPhone(String phone) {
		return phone != null && phone.matches("\\d+") && phone.length() >= 10;
	}
}