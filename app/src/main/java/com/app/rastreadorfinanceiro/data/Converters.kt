class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromColor(value: Int?): Color? { // Salvamos a cor como Inteiro (ARGB)
        return value?.let { Color(it) }
    }

    @TypeConverter
    fun colorToInt(color: Color?): Int? {
        return color?.toArgb()
    }
}