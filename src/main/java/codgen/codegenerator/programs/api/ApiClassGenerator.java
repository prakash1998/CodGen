package codgen.codegenerator.programs.api;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import codgen.codegenerator.CommonUtils;
import codgen.codegenerator.LibraryClasses;

public class ApiClassGenerator {

	public static void generate(String databaseTableName, File path) {

		String defClassName = CommonUtils.getClassNameFromTableName(databaseTableName);

		ClassName ThrowableUtil = ClassName.get("com.integ.restful.dart.common.util", "ThrowableUtil");
		ClassName RestfulResponseclass = LibraryClasses.getCommonRestfulResponse();
		ClassName DataSource = LibraryClasses.getDBUtilGenericDataSource();
		ClassName DataAccess = LibraryClasses.getDBUtilDataAccess();

		ClassName defclass = ClassName.get(CommonUtils.getDirAndPackage(path)[1] + ".immutables", defClassName);
		ClassName DAO = ClassName.get(CommonUtils.getDirAndPackage(path)[1] + ".dao", defClassName + "DAO");
		TypeName deftype = ParameterizedTypeName.get(ClassName.get(List.class), defclass);

//        List<ParameterSpec> spec = new ArrayList<>();
//        spec.add(ParameterSpec.builder(TypeName.get(String.class),"iso")
//                .addAnnotation(AnnotationSpec.builder(QueryParam.class).addMember("value","$S","iso").build()).build());
//        spec.add(ParameterSpec.builder(TypeName.get(String.class),"sandbox")
//                .addAnnotation(AnnotationSpec.builder(QueryParam.class).addMember("value","$S","sandbox").build()).build());

		MethodSpec insert = MethodSpec.methodBuilder("save" + defClassName).addModifiers(Modifier.PUBLIC)
				.returns(RestfulResponseclass).addAnnotation(CommonUtils.getAnnotationSpec(POST.class))
				.addAnnotation(CommonUtils.getAnnotationSpec(Produces.class, MediaType.APPLICATION_JSON))
				.addAnnotation(CommonUtils.getAnnotationSpec(Consumes.class, MediaType.APPLICATION_JSON))
				.addParameter(deftype, "list")
				.addStatement("$T.tryThrow(() -> $T.runDa(ds, $T.$L(list,$L.getUserPrincipal().getName())))",
						ThrowableUtil, DataAccess, DAO, "insert" + defClassName, "context")
//                    .addStatement("$T tt = new $T($S,$S,$L,$L)",Employee.class,Employee.class,"yash","ce",30000,3)
				.addStatement("return $T.$L", RestfulResponseclass, "SUCCESS")
//                    .addParameters(spec)
				.build();
		MethodSpec delMethod = MethodSpec.methodBuilder("delete" + defClassName).addModifiers(Modifier.PUBLIC)
				.returns(RestfulResponseclass).addAnnotation(CommonUtils.getAnnotationSpec(Path.class, "delete"))
				.addAnnotation(CommonUtils.getAnnotationSpec(POST.class))
				.addAnnotation(CommonUtils.getAnnotationSpec(Produces.class, MediaType.APPLICATION_JSON))
				.addAnnotation(CommonUtils.getAnnotationSpec(Consumes.class, MediaType.APPLICATION_JSON))
				.addParameter(deftype, "list").addStatement("$T.tryThrow(() -> $T.runDa(ds, $T.$L(list)))",
						ThrowableUtil, DataAccess, DAO, "delete" + defClassName)
//                    .addStatement("$T tt = new $T($S,$S,$L,$L)",Employee.class,Employee.class,"yash","ce",30000,3)
				.addStatement("return $T.$L", RestfulResponseclass, "SUCCESS").build();
		MethodSpec get = MethodSpec.methodBuilder("get" + defClassName).addException(Exception.class)
				.addModifiers(Modifier.PUBLIC).returns(deftype).addAnnotation(CommonUtils.getAnnotationSpec(POST.class))
				.addAnnotation(CommonUtils.getAnnotationSpec(Produces.class, MediaType.APPLICATION_JSON))
//                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
//                    .addStatement("return $T.$L", Arrays.class, "stream(new int[]{1,2})")
				.addStatement("return $T.tryThrow(() -> $T.runDa(ds, $T.$L()))", ThrowableUtil, DataAccess, DAO,
						"get" + defClassName)
				.build();
		FieldSpec dataSource = FieldSpec.builder(DataSource, "ds")
				.addAnnotation(CommonUtils.getAnnotationSpec(Inject.class)).build();
		FieldSpec context = FieldSpec.builder(SecurityContext.class, "context")
				.addAnnotation(CommonUtils.getAnnotationSpec(Context.class)).build();

		TypeSpec apiClass = TypeSpec.classBuilder(defClassName + "Api").addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addMethod(get).addMethod(insert).addMethod(delMethod)
				.addAnnotation(AnnotationSpec.builder(Path.class).addMember("value", "$S", "/api-path-here").build())
				.addField(dataSource).addField(context).build();

		CommonUtils.writeProgramToFile(path, apiClass);

	}

	private static String removeLast(String str) {
		if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

}
