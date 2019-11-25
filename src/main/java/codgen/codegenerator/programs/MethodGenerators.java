package codgen.codegenerator.programs;

import java.util.List;

import javax.lang.model.element.Modifier;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import codgen.codegenerator.CommonUtils;
import codgen.codegenerator.ProgramGenerator;
import codgen.codegenerator.database.connection.DatabaseConnection;

public class MethodGenerators {
	private static ClassName StringClass = ClassName.get("","String");
	private static ClassName DAOperation = ClassName.get("","DAOperation");
	private static ClassName ImmutableExtractors = ClassName.get("","ImmutableExtractors");
	private static ClassName List = ClassName.get("","List");
	private static ClassName GETAnnotation = ClassName.get("", "GET");
	private static ClassName POSTAnnotation = ClassName.get("", "POST");
	private static ClassName ProducesAnnotation = ClassName.get("", "Produces");
	private static ClassName ConsumesAnnotation = ClassName.get("", "Consumes");
	private static ClassName PathAnnotation = ClassName.get("", "Path");
    private static ClassName SQLBuilder = ClassName.get("","SQLBuilder");
    private static ClassName ModificationSupport = ClassName.get("","ModificationSupport");
    private static ClassName ImmutableDataMapper = ClassName.get("","ImmutableDataMapper");
    private static ClassName RestfulResponseclass = ClassName.get("","RestfulResponse");
    
   
    private static TypeName daoInt = ParameterizedTypeName.get(DAOperation, ArrayTypeName.of(int.class));  // DAOpertaion<int[]>
	
    public static String generateGetDao(String databaseTableName) {
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);
        ClassName defclass = ClassName.get("",defClassName);
     
        TypeName deftype = ParameterizedTypeName.get(DAOperation, ParameterizedTypeName.get(List,defclass));
    	
        MethodSpec getMethod = MethodSpec.methodBuilder("get"+defClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(deftype)
                .addStatement("return da -> da.$L($S,\n$T.$L($T.class))","get","select * " +
                        "from "+databaseTableName+" ",ImmutableExtractors,"rowExtractorOf",defclass)
                .build();
        
        return getMethod.toString();
    }
    
    public static String generateGetApi(String databaseTableName , String apiPath) {
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);

        ClassName defclass = ClassName.get("",defClassName);
        ClassName DAO = ClassName.get("",defClassName+"DAO");
        
        TypeName deftype = ParameterizedTypeName.get(DAOperation, ParameterizedTypeName.get(List,defclass));
    	
    	MethodSpec getMethod = MethodSpec.methodBuilder("get"+defClassName)
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
    
    public static String generateSaveDao(DatabaseConnection connection,String databaseTableName) {
    	List<String> primaryKeys = ProgramGenerator.getPrimaryKeys(connection, databaseTableName);
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);
        
       
        ClassName defclass = ClassName.get("",defClassName);
        TypeName listDef = ParameterizedTypeName.get(ClassName.get(List.class),defclass);
    
        String priString = "";
        for( String s: primaryKeys){
            priString += ",\""+s+"\"";
        }
        priString = priString.replaceFirst(",","");
    	
        MethodSpec insert = MethodSpec.methodBuilder("insert"+defClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(listDef, "list")
                .addParameter(StringClass,"user")
                .returns(daoInt)
                .addStatement("return da -> da.$L(\n$T.$L($S,$T.class,new $T{$L}).build()\n," +
                                "$T.$L($T.$L(list, user)))","execute",
                        SQLBuilder,"upsert",defclass.simpleName(),defclass,ArrayTypeName.of(String.class),priString
                        ,ImmutableDataMapper,"toParamVals",ModificationSupport,"withModInfo"
                )
                .build();
        
        return insert.toString();
    }
    
    public static String generateSaveApi(String databaseTableName , String apiPath) {
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);

        ClassName defclass = ClassName.get("",defClassName);
        ClassName DAO = ClassName.get("",defClassName+"DAO");
        
        TypeName deftype = ParameterizedTypeName.get(DAOperation, ParameterizedTypeName.get(List,defclass));
    	
        MethodSpec insert = MethodSpec.methodBuilder("insert"+defClassName)
                .addModifiers(Modifier.PUBLIC)
                .returns(RestfulResponseclass)
                .addAnnotation(CommonUtils.getAnnotationSpec(POSTAnnotation))
                .addAnnotation(CommonUtils.getAnnotationSpec(ProducesAnnotation, MediaType.APPLICATION_JSON))
                .addAnnotation(CommonUtils.getAnnotationSpec(ConsumesAnnotation, MediaType.APPLICATION_JSON))
                .addAnnotation(CommonUtils.getAnnotationSpec(PathAnnotation,apiPath))
                .addParameter(deftype, "list")
                .addStatement("DataAccess.runDa(ds, $T.$L(list,$L.getUserPrincipal().getName()))", DAO,"insert"+defClassName,"context")
                .addStatement("return $T.$L",RestfulResponseclass,"SUCCESS")

                .build();
    	
    	return insert.toString();
    }
    
    public static String generateDeleteDao(DatabaseConnection connection,String databaseTableName) {
      	List<String> primaryKeys = ProgramGenerator.getPrimaryKeys(connection, databaseTableName);
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);
        
       
        ClassName defclass = ClassName.get("",defClassName);
        TypeName listDef = ParameterizedTypeName.get(ClassName.get(List.class),defclass);
  
        String priString = "";
        for( String s: primaryKeys){
            priString += ",\""+s+"\"";
        }
        priString = priString.replaceFirst(",","");
        
	    MethodSpec delMethod = MethodSpec.methodBuilder("delete"+defClassName)
	            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
	            .addParameter(listDef, "list")
	            .returns(daoInt)
	            .addStatement("return da -> da.$L(\n$T.$L($S,$T.class,new $T{$L}).build()\n," +
	                            "$T.$L(list))","execute",
	                    SQLBuilder,"delete",defclass.simpleName(),defclass,ArrayTypeName.of(String.class),priString
	                    ,ImmutableDataMapper,"toParamVals"
	            )
	            .build();
    
	    return delMethod.toString();
    }
    
    public static String generateDeleteApi(String databaseTableName , String apiPath) {
    	
        String defClassName =  CommonUtils.getClassNameFromTableName(databaseTableName);

        ClassName defclass = ClassName.get("",defClassName);
        ClassName DAO = ClassName.get("",defClassName+"DAO");
        
        TypeName deftype = ParameterizedTypeName.get(DAOperation, ParameterizedTypeName.get(List,defclass));
    	
        MethodSpec delMethod = MethodSpec.methodBuilder("delete"+defClassName)
                .addModifiers(Modifier.PUBLIC)
                .returns(RestfulResponseclass)
                .addAnnotation(CommonUtils.getAnnotationSpec(POSTAnnotation))
                .addAnnotation(CommonUtils.getAnnotationSpec(ProducesAnnotation, MediaType.APPLICATION_JSON))
                .addAnnotation(CommonUtils.getAnnotationSpec(ConsumesAnnotation, MediaType.APPLICATION_JSON))
                .addAnnotation(CommonUtils.getAnnotationSpec(PathAnnotation,apiPath+"/delete"))
                .addParameter(deftype, "list")
                .addStatement("DataAccess.runDa(ds, $T.$L(list))", DAO,"delete"+defClassName)
                .addStatement("return $T.$L",RestfulResponseclass,"SUCCESS")
                .build();
        
    	return delMethod.toString();
    }
}
