/* /src/main/java/com/opencart/utilities/DataProviders.java */
package com.opencart.utilities;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "RegistrationData")
    public Object[][] getRegistrationData() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";
        String sheetName = "Data";

        ExcelUtils excel = new ExcelUtils(filePath, sheetName);
        Object[][] data = excel.getSheetDataAs2DArray();
        excel.close();
        
        // Log the data being used
        System.out.println("[INFO] Loading " + data.length + " test cases from Excel:");
        for (int i = 0; i < data.length; i++) {
            System.out.println("Test Case " + (i+1) + ": " + java.util.Arrays.toString(data[i]));
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

    private boolean isValidRegistrationData(Object[] row) {
        if (row.length < 7) return false;
        
        String firstName = row[0].toString();
        String lastName = row[1].toString();
        String email = row[2].toString();
        String phone = row[3].toString();
        String password = row[4].toString();
        String confirmPassword = row[5].toString();
        String privacyPolicy = row[6].toString();
        
        return !firstName.isEmpty() && 
               !lastName.isEmpty() && 
               isValidEmail(email) && 
               isValidPhone(phone) && 
               !password.isEmpty() && 
               password.equals(confirmPassword) && 
               "TRUE".equalsIgnoreCase(privacyPolicy);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d+") && phone.length() >= 10;
    }
}