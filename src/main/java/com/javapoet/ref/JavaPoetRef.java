package com.javapoet.ref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.persistence.Column;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class JavaPoetRef {
	public static void main(String[] args) {

		// String filePath = "C:/satandey/exceldata.xlsx";
		// List<Map<String, String>> listOfRowsInExcel =
		// readExcelData(filePath);

		String className = "DynamicEntity";
		createEmptyFile(className);
		TypeSpec dynamicEntity = TypeSpec.classBuilder("DynamicEntity").addModifiers(Modifier.PUBLIC)
				.addField(getFieldSpec("firstName")).addField(getFieldSpec("lastName"))
				.addField(getFieldSpec("ssnNumber")).addMethod(getMethodSpec("firstName"))
				.addMethod(getMethodSpec("lastName")).addMethod(getMethodSpec("ssnNumber"))
				.addMethod(setMethodSpec("firstName", "0", "30")).addMethod(setMethodSpec("lastName", "30", "30"))
				.addMethod(setMethodSpec("ssnNumber", "60", "9"))
				.addMethod(populateValuesSpec("firstName", "lastName", "ssnNumber")).build();
		System.out.println(dynamicEntity);
		writeFileContent(dynamicEntity.toString(), "DynamicEntity");
		readSigleLineData(null);
	}

	private static MethodSpec getMethodSpec(String name) {
		return MethodSpec.methodBuilder("get" + name).returns(String.class).addModifiers(Modifier.PUBLIC)
				.addStatement("return $N", name).build();
	}

	private static FieldSpec getFieldSpec(String fieldName) {
		FieldSpec fieldSpec = FieldSpec.builder(String.class, fieldName).addModifiers(Modifier.PRIVATE).build();
		return fieldSpec;

	}

	private static MethodSpec setMethodSpec(String name, String start, String length) {
		MethodSpec setMethodSpec = MethodSpec.methodBuilder("set" + name).addModifiers(Modifier.PUBLIC)
				.addAnnotation(addAnnotationSpec(start, length)).addParameter(String.class, name)
				.addStatement("this.$N = $N", name, name).build();
		return setMethodSpec;
	}

	private static AnnotationSpec addAnnotationSpec(String start, String length) {

		AnnotationSpec annotationSpec2 = AnnotationSpec.builder(Column.class).addMember("name", "$S", start)
				.addMember("length", "$N", length).build();
		return annotationSpec2;
	}

	private static MethodSpec populateValuesSpec(String firstName, String lastName, String ssnNumber) {
		MethodSpec populateValuesSpec = MethodSpec.methodBuilder("getEntityValues").addModifiers(Modifier.PUBLIC)
				.returns(DynamicEntity.class).addStatement("DynamicEntity de = new DynamicEntity()")
				.addStatement("app.set$N($N)", firstName, firstName).addStatement("de.set$N($N)", lastName, lastName)
				.addStatement("app.set$N($N)", ssnNumber, ssnNumber).addStatement("return de").build();
		return populateValuesSpec;
	}

	private static Map<String, String> readSigleLineData(String sLineData) {

		sLineData = "John weathers 546785436";
		Map<String, String> map = new HashMap<String, String>();
		String[] values = sLineData.split(" ");
		for (int i = 0; i < values.length; i++) {
			map.put("firstName", values[i].trim());
			map.put("lastName", values[++i].trim());
			map.put("ssnNumber", values[++i].trim());

		}
		return map;
	}

	private static List<Map<String, String>> readExcelData(String fileName) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			Workbook workbook = null;
			if (fileName.toLowerCase().endsWith("xlsx")) {

				workbook = new XSSFWorkbook(fis);

			} else if (fileName.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(fis);
			}

			int noOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < noOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();

				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();

					Iterator<Cell> cellIterator = row.cellIterator();
					Map<String, String> rowMap = new HashMap<String, String>();

					Cell cell = cellIterator.next();
					rowMap.put("name", cell.getStringCellValue());
					Cell cell2 = cellIterator.next();
					rowMap.put("start", cell2.getStringCellValue());
					Cell cell3 = cellIterator.next();
					rowMap.put("length", cell3.getStringCellValue());
					System.out.println(rowMap);
					list.add(rowMap);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	private static void writeFileContent(String content, String fileName) {
		String rootPath = JavaPoetRef.class.getClassLoader().getParent().getSystemResource(".").getPath();
		String destFile = rootPath + "/" + fileName + ".java";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destFile);
			fileOutputStream.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static File createEmptyFile(String fileName) {
		File file = new File(JavaPoetRef.class.getClassLoader().getParent() + "/" + fileName + ".java");
		return file;
	}
}
