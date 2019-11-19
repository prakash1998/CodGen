package codgen.codegenerator.programs.api;

import javax.lang.model.element.Modifier;
import javax.ws.rs.core.MediaType;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import codgen.codegenerator.CommonUtils;

public class GetDataGenerator {
	
    public static String generateDao(String databaseTableName) {
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);
       
        ClassName defclass = ClassName.get("",defClassName);
        ClassName DAOperation = ClassName.get("","DAOperation");
        ClassName ImmutableExtractors = ClassName.get("","ImmutableExtractors");
        ClassName List = ClassName.get("","List");
//        ClassName Exception = ClassName.get("","Exception");
        
        TypeName deftype = ParameterizedTypeName.get(DAOperation, ParameterizedTypeName.get(List,defclass));
    	
        MethodSpec getMethod = MethodSpec.methodBuilder("get"+defClassName)
//                .addException(Exception)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(deftype)
                .addStatement("return da -> da.$L($S,\n$T.$L($T.class))","get","select * " +
                        "from "+databaseTableName+" ",ImmutableExtractors,"rowExtractorOf",defclass)
                .build();
        
        return getMethod.toString();
    }
    
    public static String generateApi(String databaseTableName , String apiPath) {
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);
        
//        ClassName ImmutableExtractors = ClassName.get("","ImmutableExtractors");
//        ClassName Exception = ClassName.get("","Exception");
        ClassName defclass = ClassName.get("",defClassName);
        ClassName DAOperation = ClassName.get("","DAOperation");
        ClassName List = ClassName.get("","List");
        ClassName GETAnnotation = ClassName.get("", "GET");
        ClassName ProducesAnnotation = ClassName.get("", "Produces");
        ClassName PathAnnotation = ClassName.get("", "Path");
        ClassName DAO = ClassName.get("",defClassName+"DAO");
        
        TypeName deftype = ParameterizedTypeName.get(DAOperation, ParameterizedTypeName.get(List,defclass));
    	
    	MethodSpec getMethod = MethodSpec.methodBuilder("get"+defClassName)
//            .addException(Exception)
            .addModifiers(Modifier.PUBLIC)
            .returns(deftype)
            .addAnnotation(CommonUtils.getAnnotationSpec(GETAnnotation))
            .addAnnotation(CommonUtils.getAnnotationSpec(ProducesAnnotation, MediaType.APPLICATION_JSON))
            .addAnnotation(CommonUtils.getAnnotationSpec(PathAnnotation,apiPath))
            .addStatement("pmgrIdentity(context).validatePermission(place-permissions-here)")
            .addStatement("DataAccess.runDa(ds, $T.$L()))", DAO,"get"+defClassName)
            .build();
    	
    	return getMethod.toString();
    }

}
