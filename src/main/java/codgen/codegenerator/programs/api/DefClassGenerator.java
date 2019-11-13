package codgen.codegenerator.programs.api;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import codgen.codegenerator.CommonUtils;
import codgen.codegenerator.LibraryClasses;
import codgen.codegenerator.database.connection.DatabaseConnection;
import codgen.codegenerator.database.query.TableColumn;
import codgen.codegenerator.database.query.TableInformationFetcher;

public class DefClassGenerator {

    public static void generateClassFor(DatabaseConnection connection, String databaseTableName, File path) throws Exception{

        List<TableColumn> columns =  TableInformationFetcher.fetch(connection,databaseTableName);

        String actualClassName = CommonUtils.getClassNameFromTableName(databaseTableName);
        String defClassName = actualClassName +"Def";

        ClassName ModificationInfo = LibraryClasses.getDBUtilModificationInfo();
        ClassName ModifiableData = LibraryClasses.getDBUtilModifiableData();

//        ClassName valueAnnotation = ClassName.get("org.immutables.value","Value");
        ClassName valueStyleAnnotation = ClassName.get("org.immutables.value","Value" ,"Style");
        ClassName valueImmutableAnnotation = ClassName.get("org.immutables.value","Value","Immutable");
        ClassName valueAuxiliaryAnnotation = ClassName.get("org.immutables.value","Value","Auxiliary");
        ClassName JsonDeserialize= ClassName.get("com.fasterxml.jackson.databind.annotation","JsonDeserialize");
        ClassName Mapper= ClassName.get("com.integ.db.mapper","Mapper");



        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(defClassName)
                .addModifiers(Modifier.PUBLIC);

        for(TableColumn column : columns){
            if(!column.getColumnName().equals("MODIFIER") && !column.getColumnName().equals("MODIFIEDDATE")) {




                MethodSpec.Builder method = MethodSpec.methodBuilder(column.getColumnNameCamelCase())
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

                method.addAnnotation(AnnotationSpec.builder(Mapper)
                        .addMember("colName" ,"\"$L\"" , column.getColumnName())
                        .build());

                if (column.isNullable())
                    method.returns(ParameterizedTypeName.get(Optional.class, column.getColumnDataType()));
                else
                    method.returns(column.getColumnDataType());


                builder.addMethod(method.build());
            }
        }



        MethodSpec.Builder modificationinfo = MethodSpec.methodBuilder("modificationInfo")
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(valueAuxiliaryAnnotation)
                .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), ModificationInfo));

        builder.addMethod(modificationinfo.build());


//        MethodSpec methodToAddValueAnnotation = MethodSpec.methodBuilder("toAddValueAnnotaion")
//                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
//                .addAnnotation(valueAnnotation)
//                .addStatement("error_remove this method")
//                .build();
//
//        builder.addMethod(methodToAddValueAnnotation);



        TypeSpec defClass = builder.addSuperinterface(ModifiableData)
                .addAnnotation(AnnotationSpec.builder(valueStyleAnnotation)
                        .addMember("typeAbstract","$S","*Def")
                        .addMember("typeImmutable","$S","*")
                        .build())
                .addAnnotation(valueImmutableAnnotation)
                .addAnnotation(AnnotationSpec.builder(JsonDeserialize)
                        .addMember("builder" ,"$L.Builder.class" , actualClassName)
                        .build()
                )
                .addAnnotation(LibraryClasses.getDBUtilMappableImmutable())
                .build();


        CommonUtils.writeProgramToFile(path,defClass);

    }

    private static  String removeLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
