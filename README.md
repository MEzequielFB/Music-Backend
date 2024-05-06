
# Music Backend

Credenciales de cuenta de administrador y delivery:

    Cuenta admin: bianca@gmail.com | password123
    Cuenta super_admin: superadmin@gmail.com | password123
    Cuenta de delivery: delivery@gmail.com | password123

## Introducción
API de música hecha con Spring Boot que cuenta con:

- Spring Security y autenticación vía JWT Token
- Autorización en base al rol del usuario logueado
- Comunicación entre microservicios
- Manejo de excepciones
- Documentación con Swagger

## Microservicios
### [AuthenticationMS](https://authenticationms.onrender.com/swagger-ui/index.html#/)
Se encarga de la autenticación y registro de usuarios mediante Spring Security y JWT Token. Se comunica con el microservicio de usuario para el registro y autenticación de los usuarios.

### [UserMS](https://music-render-pvrq.onrender.com/swagger-ui/index.html#)
Se encarga de la gestión de usuarios, cuentas bancarias y roles de usuarios.

### [MusicMS](https://musicms.onrender.com/swagger-ui/index.html#/)
Se encarga de la gestión de artistas, álbumes, playlists, canciones y géneros musicales.

### [MerchandisingMS](https://mechandisingms.onrender.com/swagger-ui/index.html#/)
Se encarga de la gestión de productos, carritos de compra, órdenes de compra, estados de las órdenes y las etiquetas de los productos.

## Roles
La API cuenta con distintos roles de usuarios que otorgan permisos para realizar ciertas acciones.
- USER:
    - Buscar canciones/playlists/albums/artistas
    - Escuchar canciones
    - Crear playlists
    - Crear cuenta bancaria
    - Comprar productos
- ARTIST: 
    - Buscar canciones/playlists/álbumes/artistas
    - Escuchar canciones
    - Crear canciones/álbumes
- DELIVERY:
    - Gestionar órdenes de compra
    - Visualizar estados de órdenes y etiquetas
- ADMIN:
    - Gestionar usuarios, cuentas bancarias, roles, estados de órdenes, órdenes de compra, carritos de compras, productos y etiquetas
- SUPER_ADMIN:
    - Gestionar usuarios, cuentas bancarias, roles, estados de órdenes, órdenes de compra, carritos de compras, productos y etiquetas
    - Cambiar roles de usuarios (USER, ADMIN, DELIVERY)

## Información adicional
[Modelo físico de la base de datos (descarga)](https://res.cloudinary.com/dvfmykwam/image/upload/v1714420283/music-model.svg)

