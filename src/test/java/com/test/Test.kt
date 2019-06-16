package com.test

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "_type")
@JsonSubTypes(
        JsonSubTypes.Type(value = Data::class, name = "V1")
)
interface AbstractData

data class Data(
        val count: Int
): AbstractData


class Model<T> (
        val data: T
)

class Test {

    @Test
    fun should_include_type() {

        val model = Model<Data> (
                data = Data(1)
        )

        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())

        val modelJson = mapper.writeValueAsString(model)
        println(modelJson)

        // Here fails with error:
        // com.fasterxml.jackson.databind.exc.InvalidTypeIdException: Missing type id when trying to resolve subtype of [simple type, class com.test.AbstractData]: missing type id property '_type' (for POJO property 'data')
        val modelFromString: Model<AbstractData> = mapper.readValue(modelJson)

    }

}