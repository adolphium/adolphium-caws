### Requirements

- Java 17
- environment variables for application.yml:
    - notification sender mail configuration:
        - `MAIL_HOST` - smtp email host
        - `MAIL_PORT` - smtp email port
        - `MAIL_USERNAME` - mail username (should be an email)
        - `MAIL_PASSWORD` - mail password
    - db connection:
        - `CAWS_DB_URL` defaults to `jdbc:postgresql://localhost:5432/caws_db`
        - `CAWS_DB_USER` defaults to `caws_db_user`
        - `CAWS_DB_PASSWD` defaults to `caws_db_passwd`
    - test db connection
        - `CAWS_TEST_DB_URL` defaults to `jdbc:postgresql://localhost:5433/caws_test_db`
        - `CAWS_TEST_DB_USER` defaults to `caws_test_db_user`
        - `CAWS_TEST_DB_PASSWD` defaults to `caws_test_db_passwd`

      Note: Environment variables can be set stored in the environment of the system where the application is running. In windows you can set them by using the Registry Editor, which may need a PC restart. In linux you can set them otherwise. Please DYOR. The mail authentication still works, I tested it with Gmail recently, though the way sending mails is implemented in this application is deprecated.

### Docker config

- environment variables for docker-compose.yml:
  - db
      - `POSTGRES_DB` defaults to `caws_db`
      - `CAWS_DB_USER` defaults to `caws_db_user`
      - `CAWS_DB_PASSWD` defaults to `caws_db_passwd`
  - test db
    - `CAWS_TEST_DB_NAME` defaults to `caws_test_db`
    - `CAWS_TEST_DB_USER` defaults to `caws_test_db_user`
    - `CAWS_TEST_DB_PASSWD` defaults to `caws_test_db_passwd`
- for now the db port 5432 is hardcoded

###  

### How to run:
 Docker or Rancher or similar must be started. The services in docker-compose need to be run, where you can find a docker image with a database config for test running purposes and a database for the application to run. If the docker images run and the environment variables are set, the application should run wit the command mvn spring-boot:run (maven needs to be installed). This application runs with Java 17.  

* TODO input safety checks -> no unfiltered sending of email content
* TODO easy db port configuration, so 5432 is not hardcoded in docker-compose.yml
