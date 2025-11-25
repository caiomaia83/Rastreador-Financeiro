@Entity(tableName = "categories")
data class CategoryModel(
    @PrimaryKey val id: String,
    val name: String,
    val color: Color // O Converter cuidará disso
)