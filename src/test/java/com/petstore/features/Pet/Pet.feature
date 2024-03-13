
Background: Iniciar Precondiciones
Given url "https://petstore.swagger.io/v2"

Scenario: Crear un usuario
    # https://petstore.swagger.io/v2/user
Given url "https://petstore.swagger.io/v2/user"
And request {"id": 1991,"username": "cinthya","firstName": "string","lastName": "string","email": "string","password": "string","phone": "string","userStatus": 0}
When method POST
Then status 200

Scenario: Buscar el usuario creado
Given url "https://petstore.swagger.io/v2/user/cinthya"
When method GET
Then status 200
And print response

Scenario: Actualizar el nombre y el correo del usuario
Given url "https://petstore.swagger.io/v2/user/cinthya"
And request {"id": 1991,"username": "carolina","firstName": "string","lastName": "string","email": "cinthya.andrademos@hotmail.com","password": "string","phone": "string","userStatus": 0}
When method PUT
Then status 200

Scenario: Buscar el usuario actualizado
Given url "https://petstore.swagger.io/v2/user/carolina"
When method GET
Then status 200
And print response

Scenario: Eliminar el usuario
Given url "https://petstore.swagger.io/v2/user/carolina"
When method DELETE
Then status 200

