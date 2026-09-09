#!/bin/bash

sbt clean scalafmtAll scalafmtSbt coverage test scalafmtCheckAll coverageReport