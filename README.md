# JHawanetFramework

Este software permite la soluci�n de problemas relacionados con redes de distribuci�n de agua potable. Los problemas que el programa incluye por defecto son
+ El problema monoobjetivo de costo de inversion
+ El problema multiobjetivo de de regimen de bombeo, cuyos objetivos son el costo energetico y el costo de mantenimiento.

## Como instalar

Los pasos para instalar el software son los siguientes:

1. Instalar maven. Una guia de como instalarlo puede encontrarla [aqu�](hhttps://maven.apache.org/index.html)
2. Una vez maven este instalado, hay que agregar el jar de la EpanetTolkit requerido para la simulaci�n al repositorio de maven. Para esto ejecute el siguiente comando: 

```
mvn install:install-file -Dfile="lib/epajava.jar" -DgroupId=epajava -DartifactId=epajava -Dversion="1.0" -Dpackaging=jar
```
3. Ahora ejecute el comando:
```
mvn clean package
``` 
4. Despues de terminar la ejecuci�n del comando el lanzador se encontrara en target/jfx/

5. Para volver a compilar solo hace falta ejecutar nuevamente el paso 3.

**NOTA**: Para poder ejecutar desde un IDE que permita proyectos maven se necesita haber realizado hasta el paso 2.

## Otras maneras de compilar
Compile with mvn jfx:jar to create a jar -> este comando realizara lo mismo que mvn package

Compile with mvn jfx:native to create a native -> genera un exe y si est�n instalados los programas correctos tambi�n generara un instalador.

## Abrir proyecto con eclipse
Abrir el proyecto desde eclipse y agregar la ruta lib/additionalResources y la carpeta src/resource, en caso de que no se encuentre, al *build path*. Para ello, seleccione la carpeta y muestre el men� contextual. Luego, en la opci�n *Build path* seleccione la opci�n *Use as source folder* (Puede cambiar la configuraci�n tambi�n desde *Configure build path*). Esto se muestra en la imagen a continuaci�n:

![Agregar path](assets/addPath.png)

El *build path* deber�a mostrarse de la siguiente manera:

![Build path](assets/BuildPath.png)

## Abrir Proyecto en IntelliJ
Abrir o exportar la carpeta del proyecto en IntelliJ. Para esto hay dos formas de hacerlo. La primera consiste en hacerlo desde la ventana de bienvenida usando la opci�n *Open or import*:

![Ventana de bienvenida](assets/WelcomeViewIJ.png)

o desde un proyecto ya abierto ir a File > Open:

![Open](assets/IntelliJOpen.png)

Una vez abierto el proyecto hay que revisar si las carpetas est�n correctamente configuradas. Para ello, es necesario agregar la carpeta lib/additionalResources y la carpeta src/resource en la configuraci�n de la estructura del proyecto en caso de que no se encuentren ya agregadas. Para configurar la estructura del proyecto ve a File > Proyect Structure:

![Menu estructura del proyecto](assets/ProyectStructureMenu.png)

y en *Modules* revisa que este de la siguiente manera:

![Estructura del proyecto](assets/ProyectStructure.png)

en caso de que no se encuentre la configuraci�n de la manera indicada busca la carpeta deseada y seleccionala. Luego, has click sobre el bot�n *Resources*

![Estructura del proyecto](assets/ProyectStructureAddResource.png)

con esto el proyecto estar� listo para ser usado en IntelliJ. 

### Nota 1:
 Assets es solo una carpeta donde se suben las im�genes. No es usada por el proyecto.
### Nota 2:
 La version en ingles y la version en espa�ol de epanet a veces ocupan distintas palabras claves (Ej: Feet en ingles y Pies en espa�ol). Este programa lee el formato inp de la version en ingles.
### Nota 3:
 La ventaja de usar IntelliJ es que este posee un analizador de c�digo que en mi opini�n es bastante util y permite detectar el uso de algunas anotaciones extras para verificar el codigo como @NotNull y @Nullable. En caso de que el proyecto sea abierto usando eclipse estas anotaciones no tienen uso pero no causaran problemas en la compilaci�n del proyecto.

### Nota 4:
Dependiendo de la unidad de flujo (*Flow*) escogida, el sistema de unidades cambia. Para m�s informaci�n ver en el manual de epanet la secci�n *Units of Measurement*. A continuaci�n tambi�n se adjuntas unas imagenes referentes a esto:

![SI Metric](assets/SIMetric.png)

![US Units](assets/USUnits.png)

### Nota 5:
Se debe tener cuidado con el *encoding* de los archivos. Por defecto, el programa lee ISO-8859-1. Pero para evitar cualquier error, mejor asegurar que los archivos de red no contengan caracteres raros como �,�, etc.

### Nota 6:
Si se agrega el atributo *OverallConstraintViolation* para penalizar las soluciones, este atributo sera recuperado y mostrado en la ventana de resultados.

### Nota 7: Para desarrolladores principalmente
Tener cuidado con cerrar epanet (epanet.ENclose). Si est� cerrado hacer una llamada a la librer�a puede causar que el programa se cierre y no lanzara ninguna advertencia m�s que un c�digo de error. 

