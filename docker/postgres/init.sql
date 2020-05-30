CREATE USER keycloak_user WITH ENCRYPTED PASSWORD 'keycloak_pass';
CREATE DATABASE keycloak;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak_user;

CREATE USER restaurnant_menu_user WITH ENCRYPTED PASSWORD 'restaurnant_menu_pass';
CREATE DATABASE restaurnant_menu;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak_user;

CREATE USER restaurnant_orders_user WITH ENCRYPTED PASSWORD 'restaurnant_orders_pass';
CREATE DATABASE restaurnant_orders;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak_user;
