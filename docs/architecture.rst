System Architecture
==================

MediSys follows a modern, layered architecture pattern designed for scalability, maintainability, and performance.

Architecture Overview
---------------------

.. code-block:: text

    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
    │   JavaFX GUI    │    │  Spring Boot    │    │   PostgreSQL    │
    │   Controllers   │◄──►│   Services      │◄──►│   Database      │
    └─────────────────┘    └─────────────────┘    └─────────────────┘
             │                       │                       │
             │              ┌─────────────────┐              │
             │              │  Redis Cache    │              │
             └──────────────┤   Layer         ├──────────────┘
                            └─────────────────┘
                                     │
                            ┌─────────────────┐
                            │  Monitoring &   │
                            │  Metrics        │
                            └─────────────────┘

Technology Stack
----------------

Frontend
~~~~~~~~

* **JavaFX 22**: Modern desktop UI framework
* **FXML**: Declarative UI layouts
* **CSS**: Professional styling and theming
* **Scene Builder**: Visual FXML editor

Backend
~~~~~~~

* **Spring Boot 3.3.0**: Enterprise application framework
* **Spring Security**: Authentication and authorization
* **Spring Data JPA**: Data persistence layer
* **HikariCP**: High-performance connection pooling

Database
~~~~~~~~

* **PostgreSQL 16.9**: Primary production database
* **H2**: Embedded database for development
* **Redis**: Caching and session management
* **Flyway**: Database migration management

Design Patterns
---------------

Model-View-Controller (MVC)
~~~~~~~~~~~~~~~~~~~~~~~~~~~

The application follows the MVC pattern with clear separation of concerns:

* **Model**: Entity classes representing business data
* **View**: JavaFX FXML files defining the user interface
* **Controller**: Java classes handling user interactions and business logic

Service Layer Pattern
~~~~~~~~~~~~~~~~~~~~~

Business logic is encapsulated in service classes that provide:

* **Data validation**: Input validation and business rules
* **Transaction management**: Database transaction handling
* **Business operations**: Core healthcare management operations
* **Integration**: External service integration points

Repository Pattern
~~~~~~~~~~~~~~~~~~

Data access is abstracted through repository interfaces:

* **CRUD operations**: Standard create, read, update, delete operations
* **Custom queries**: Specialized data retrieval methods
* **Database abstraction**: Independence from specific database implementations
* **Testing support**: Easy mocking for unit tests
