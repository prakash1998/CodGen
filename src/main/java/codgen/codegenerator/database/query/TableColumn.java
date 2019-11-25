package codgen.codegenerator.database.query;


import com.google.common.base.CaseFormat;

public class TableColumn {
    private int columnId;
    private String columnNameCamelCase;
    private String columnName;
    private Class<?> columnDataType;
    private boolean isNullable;
    private boolean isPrimaryKey;

    public TableColumn(int columnId, String columnName, Class<?> columnDataType, boolean isNullable , boolean isPrimaryKey) {
        this.columnId = columnId;
        this.columnName = columnName;
        this.columnNameCamelCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,columnName);
        this.columnDataType = columnDataType;
        this.isNullable = isNullable;
        this.isPrimaryKey = isPrimaryKey;
    }

    public int getColumnId() {
        return columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnNameCamelCase() {
        return columnNameCamelCase;
    }

    public Class getColumnDataType() {
        return columnDataType;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    
    @Override
    public String toString() {
        return "TableColumn{" +
                "columnId=" + columnId +
                ", columnName='" + columnName + '\'' +
                ", columnDataType='" + columnDataType + '\'' +
                ", isNullable=" + isNullable +
                '}';
    }
}
