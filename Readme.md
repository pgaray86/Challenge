# Directa24 Back-End Developer Challenge 

In this challenge, the REST API contains information about a collection of movie released after the year 2010, directed by acclaimed directors.  
Given the threshold value, the goal is to use the API to get the list of the names of the directors with most movies directed. Specifically, the list of names of directors with movie count strictly greater than the given threshold.   
The list of names must be returned in alphabetical order.  

To access the collection of users perform HTTP GET request to:
https://directa24-movies.wiremockapi.cloud/api/movies/search?page=<pageNumber>
where <pageNumber> is an integer denoting the page of the results to return.

For example, GET request to:
https://directa24-movies.wiremockapi.cloud/api/movies/search?page=2
will return the second page of the collection of movies. Pages are numbered from 1, so in order to access the first page, you need to ask for page number 1.
The response to such request is a JSON with the following 5 fields:

- page: The current page of the results  
- per_page: The maximum number of movies returned per page.  
- total: The total number of movies on all pages of the result.  
- total_pages: The total number of pages with results.  
- data: An array of objects containing movies returned on the requested page  

Each movie record has the following schema:  
- Title: title of the movie  
- Year: year the movie was released  
- Rated: movie rating  
- Released: movie release date  
- Runtime: movie duration time in minutes  
- Genre: move genre  
- Director: movie director  
- Writer: movie writers  
- Actors: movie actors  

## Posible Solutions
### Clone repository and complete the Function
  
Complete the function:

    List<String> getDirectors(int threshold)

getDirectors has the following parameter:
- threshold: integer denoting the threshold value for the number movies a person has directed

The function must return a list of strings denoting the name of the directors whose number of movies directed is strictly greater than the given threshold. 
The directors name in the list must be ordered in alphabetical order.


#### Sample Input For Custom Testing
    4  
#### Sample Output
    Martin Scorsese
    Woody Allen
    
The threshold value is 4, so the result must contain directors names with more than 4 movies directed.   
There are 2 such directors and names in the alphabetical order listed in Sample Output.

### Alternative: Spring Boot solution

An alternative solution to clonning the repository and implementing the function can be creating a new Spring Boot project with Rest endpoint:       
```
/api/directors?threshold=X
```

Sample : `/api/directors?threshold=4`

Json response:
```
{  "directors": ["Martin Scorsese","Woody Allen"] }
```

The solution can be shared via .zip file or sharing a github repository url via email. 



### Solución
* Se usa Spring Boot en su versión 3.2.4.
* Se han creado los DTOs (Data Transfer Objects) con la estructura de la respuesta de la REST API:
    - `Movie`
    - `ResponseApi`
* Se ha creado una configuración (`@Configuration`) para definir dos Beans que serán utilizados en el servicio:
    - `RestTemplate`
    - `ObjectMapper`
* En `application.properties`, se ha añadido una propiedad para la URL base de la API externa que se va a consumir (`app.external.service-url`).
* Para realizar llamadas a la REST API se ha utilizado `RestTemplate`, incluido en Spring Boot.
* Se ha desarrollado un servicio (`DirectorServiceImp`) que implementa la lógica de llamada a la REST API:
    - En primer lugar, se inyectan `RestTemplate` y `ObjectMapper` usando `@Autowired`.
    - Se utiliza `@Value` para obtener el valor de la propiedad definida en `application.properties`.
    - En el método `fetchAllMovies`, se realiza la primera llamada a la API para obtener la página 1, se verifica el número total de páginas y, si hay más de una, se realizan las llamadas adicionales y se almacenan los datos en una lista.
    - En el método `getDirectorNames`, se cuenta el número de películas por director utilizando un `Map`.
    - Después de obtener el conteo de películas por director, se filtran aquellos que tienen un número de películas mayor al parámetro `threshold`.
    - Los nombres de los directores filtrados se ordenan alfabéticamente antes de devolver la lista.
* Se ha creado un controlador (`DirectorController`) que responde a las llamadas GET en la ruta `/api/directors?threshold=4`.
* Se ha desarrollado un `GlobalExceptionHandler` que intercepta las excepciones y responde adecuadamente a los errores:
    - `MissingServletRequestParameterException` para manejar casos donde faltan parámetros requeridos.
    - `MethodArgumentTypeMismatchException` para manejar casos donde los parámetros no tienen el tipo esperado.
    - `HttpClientErrorException` para manejar errores en llamadas a servicios externos.

### Lo que se puede mejorar
* **Pruebas Unitarias y de Integración**: Añadir pruebas unitarias y de integración para el servicio `DirectorServiceImp` y el controlador `DirectorController` para asegurar que el comportamiento es el esperado y facilitar el mantenimiento del código.
* **Cache**: Implementar una capa de cache para almacenar las respuestas de la API externa y reducir el número de llamadas a la API, mejorando así la eficiencia y reduciendo la latencia.
* **Documentación y Swagger**: Integrar Swagger para la generación automática de documentación de la API REST, lo que facilitaría el uso y la comprensión del servicio por parte de otros desarrolladores.
* **Seguridad**: Añadir autenticación y autorización a los endpoints del controlador para asegurar que solo los usuarios autorizados puedan acceder a los datos.
* **Optimización del Mapeo de Directores**: Mejorar el método `getDirectorNames` utilizando streams y lambdas de Java 8 para hacer el código más conciso y posiblemente más eficiente.