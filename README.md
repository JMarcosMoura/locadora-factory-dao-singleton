LOCADORA - Factory + DAO + JPA (Neon)
====================================

1) Configurar Neon: criar projeto e copiar JDBC URL, user e password.
2) Editar src/main/resources/META-INF/persistence.xml e substituir DB_URL, DB_USER, DB_PASS.
   A JDBC URL deve conter sslmode=require para Neon.
3) Compilar e executar:
   mvn clean package
   mvn exec:java

Projeto criado para Java 17 e Maven. Terminal sem acentos para compatibilidade.
