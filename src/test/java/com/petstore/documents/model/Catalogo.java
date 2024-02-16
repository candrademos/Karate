package com.petstore.documents.model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Data
public class Catalogo {
    @BsonProperty(value = "_id")
    private ObjectId generatedId;
    @BsonProperty(value = "id")
    private Integer id;
    @BsonProperty(value = "nombre")
    private String nombre;
    @BsonProperty(value = "mnemonico")
    private String mnemonico;
    @BsonProperty(value = "valor_cadena")
    private String valorCadena;
    @BsonProperty(value = "valor_numero")
    private Integer valorNumero;


}
