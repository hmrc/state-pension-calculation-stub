
# state-pension-calculation-stub

## Running locally
Start state-pension-calculation locally in root folder/terminal
```bash
sbt "run 9791"
```
Start microservices using service manager
```bash
sm2 --start GYSP_ALL
```

## Test Data

This stub is dynamic so does not contain any hardcoded test data. You can insert a mirror of the staging environment's data using the `insert-staging-data.sh` script.
```bash
./insert-staging-data.sh
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
