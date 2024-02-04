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

* TODO input safety checks -> no unfiltered sending of email content
* TODO easy db port configuration, so 5432 is not hardcoded in docker-compose.yml
