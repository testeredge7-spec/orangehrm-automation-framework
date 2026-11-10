package utilities;

import java.io.File;
import java.util.List;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	private static final String FILE_PATH = "TestData/TestData.xlsx";

	public static final String USER_SHEET = "UserData";

	public static final String RESULT_SHEET = "TestResults";
	
	public static final String RECORDS_SHEET = "UserRecords";

	private static final String[] USER_HEADERS = { "Purpose", "UserRole", "EmployeeName", "Status", "Username",
			"Password", "ConfirmPassword", "EditedStatus" };

	private static final String[][] USER_ROWS = {
			{ "ValidUser", "Admin", "a", "Enabled", "autouser", "Test@12345", "Test@12345", "Disabled" },
			{ "PasswordMismatch", "Admin", "a", "Enabled", "mismatchuser", "Test@12345", "Test@54321", "Disabled" },
			{ "WeakPassword", "Admin", "a", "Enabled", "weakuser", "abc", "abc", "Disabled" } };

	private static final String[] RESULT_HEADERS = { "TestCase", "Status", "ExecutedOn" };

	private static synchronized void createFileIfMissing() throws IOException {

		File file = new File(FILE_PATH);

		if (file.exists()) {

			return;
		}

		file.getParentFile().mkdirs();

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			Sheet userSheet = workbook.createSheet(USER_SHEET);

			Row userHeader = userSheet.createRow(0);

			for (int i = 0; i < USER_HEADERS.length; i++) {

				userHeader.createCell(i).setCellValue(USER_HEADERS[i]);
			}

			for (int r = 0; r < USER_ROWS.length; r++) {

				Row row = userSheet.createRow(r + 1);

				for (int c = 0; c < USER_ROWS[r].length; c++) {

					row.createCell(c).setCellValue(USER_ROWS[r][c]);
				}
			}

			Sheet resultSheet = workbook.createSheet(RESULT_SHEET);

			Row resultHeader = resultSheet.createRow(0);

			for (int i = 0; i < RESULT_HEADERS.length; i++) {

				resultHeader.createCell(i).setCellValue(RESULT_HEADERS[i]);
			}

			try (FileOutputStream fos = new FileOutputStream(file)) {

				workbook.write(fos);
			}
		}
	}

	public static synchronized void writeResult(String testCase, String status) {

		try {

			createFileIfMissing();

			XSSFWorkbook workbook;

			try (FileInputStream fis = new FileInputStream(FILE_PATH)) {

				workbook = new XSSFWorkbook(fis);
			}

			Sheet sheet = workbook.getSheet(RESULT_SHEET);

			Row row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0).setCellValue(testCase);

			row.createCell(1).setCellValue(status);

			row.createCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {

				workbook.write(fos);
			}

			workbook.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static synchronized String getCellData(String sheetName, int rowNum, int colNum) {

		try {

			createFileIfMissing();

			try (FileInputStream fis = new FileInputStream(FILE_PATH); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

				Sheet sheet = workbook.getSheet(sheetName);

				Row row = sheet.getRow(rowNum);

				if (row == null) {

					return "";
				}

				Cell cell = row.getCell(colNum);

				if (cell == null) {

					return "";
				}

				return new DataFormatter().formatCellValue(cell);
			}

		} catch (IOException e) {

			e.printStackTrace();

			return "";
		}
	}

	public static synchronized int getRowCount(String sheetName) {

		try {

			createFileIfMissing();

			try (FileInputStream fis = new FileInputStream(FILE_PATH); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

				return workbook.getSheet(sheetName).getLastRowNum();
			}

		} catch (IOException e) {

			e.printStackTrace();

			return 0;
		}
	}

	public static String getUserData(int rowNum, String columnName) {

		for (int i = 0; i < USER_HEADERS.length; i++) {

			if (USER_HEADERS[i].equalsIgnoreCase(columnName)) {

				return getCellData(USER_SHEET, rowNum, i);
			}
		}

		return "";
	}
	public static synchronized boolean writeUserRecords(List<String[]> records) {

		try {

			createFileIfMissing();

			XSSFWorkbook workbook;

			try (FileInputStream fis = new FileInputStream(FILE_PATH)) {

				workbook = new XSSFWorkbook(fis);
			}

			int existing = workbook.getSheetIndex(RECORDS_SHEET);

			if (existing >= 0) {

				workbook.removeSheetAt(existing);
			}

			Sheet sheet = workbook.createSheet(RECORDS_SHEET);

			String[] headers = { "S.No", "Username", "User Role", "Employee Name", "Status" };

			Row headerRow = sheet.createRow(0);

			for (int i = 0; i < headers.length; i++) {

				headerRow.createCell(i).setCellValue(headers[i]);
			}

			for (int i = 0; i < records.size(); i++) {

				Row row = sheet.createRow(i + 1);

				row.createCell(0).setCellValue(i + 1);

				for (int c = 0; c < records.get(i).length; c++) {

					row.createCell(c + 1).setCellValue(records.get(i)[c]);
				}
			}

			sheet.setColumnWidth(0, 8 * 256);

			sheet.setColumnWidth(1, 40 * 256);

			sheet.setColumnWidth(2, 18 * 256);

			sheet.setColumnWidth(3, 30 * 256);

			sheet.setColumnWidth(4, 14 * 256);

			try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {

				workbook.write(fos);
			}

			workbook.close();

			return true;

		} catch (IOException e) {

			e.printStackTrace();

			return false;
		}
	}
}