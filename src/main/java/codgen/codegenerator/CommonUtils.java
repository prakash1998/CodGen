package codgen.codegenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class CommonUtils {

	public static String injectImports(JavaFile javaFile, List<String> imports) {
		String rawSource = javaFile.toString();

		List<String> result = new ArrayList<>();
		for (String s : rawSource.split("\n", -1)) {
			result.add(s);
			if (s.startsWith("package ")) {
				result.add("");
				for (String i : imports) {
					result.add("import " + i + ";");
				}
			}
		}
		return String.join("\n", result);
	}

	private static String removeLast(String str) {
		if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '.') {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static String[] getDirAndPackage(File path) {

		String fullPath = path.getPath();

		String[] sp = fullPath.split("\\\\");

		String packageName = "";
		String dir = "";
		boolean flag = true;
		for (String a : sp) {

			if (flag) {
				dir = dir + a + "\\";
			} else {
				packageName = packageName + a + ".";
			}

			if (a.equals("java")) {
				flag = false;
			}
		}

		if (dir.length() > 0)
			dir = removeLast(dir);

		if (packageName.length() > 0)
			packageName = removeLast(packageName);

		return new String[] { dir, packageName };
	}
	
	public static void writeProgramToFile(File path, TypeSpec typeSpec) throws IOException {

		String[] temp = CommonUtils.getDirAndPackage(path);

		String directory = temp[0];
		String packageName = temp[1];

		JavaFile file = JavaFile.builder(packageName, typeSpec).build();

//        file.writeTo(System.out);
		
		
		file.writeTo(new File(directory));
	}

	public static String getClassNameFromTableName(String tableName) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
	}

	public static AnnotationSpec getAnnotationSpec(Class name) {
		return getAnnotationSpec(name, "");
	}

	public static AnnotationSpec getAnnotationSpec(Class name, String value) {
		if (value.equals(""))
			return AnnotationSpec.builder(name).build();
		return AnnotationSpec.builder(name).addMember("value", "$S", value).build();
	}

	public static AnnotationSpec getAnnotationSpec(ClassName name) {
		return getAnnotationSpec(name, "");
	}

	public static AnnotationSpec getAnnotationSpec(ClassName name, String value) {
		if (value.equals(""))
			return AnnotationSpec.builder(name).build();
		return AnnotationSpec.builder(name).addMember("value", "$S", value).build();
	}
	
}
