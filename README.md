# SSL Checker CLI

A modern Java CLI tool for checking SSL/TLS certificate expiry and details.

## Features
- Check single domain or bulk list
- Warning for imminent expiration
- JSON output for CI/CD
- Native image support via GraalVM

## Usage
### Single Check
```bash
java -jar target/ssl-checker-1.0.0.jar check google.com
```

### Bulk Check
```bash
java -jar target/ssl-checker-1.0.0.jar bulk google.com example.com
```

### JSON Output
```bash
java -jar target/ssl-checker-1.0.0.jar check google.com --json
```

## Build fat JAR
mvn clean package

## Run with Java
java -jar target/ssl-checker-1.0.0.jar check google.com

## Build native image (requires GraalVM)
mvn native:compile

## Run native binary
./target/ssl-checker check google.com --warn 60


## Single domain check
$ ssl-checker check google.com
✓ google.com:443
  Subject:    CN=*.google.com
  Issuer:     CN=GTS CA 1C3, O=Google Trust Services LLC, C=US
  Valid:      2024-01-15 to 2024-04-15
  Expires in: 45 days
  Protocol:   TLSv1.3
  SANs:       *.google.com, *.appengine.google.com, *.bdn.dev, ...


## JSON output for CI/CD
$ ssl-checker check expired.badssl.com --json
{
  "host": "expired.badssl.com",
  "port": 443,
  "error": "Certificate expired 365 days ago",
  "checkedAt": "2024-02-09T10:30:00Z"
}

## Bulk check with file
$ cat domains.txt
google.com:443
github.com
expired.badssl.com:443

$# ssl-checker bulk -f domains.txt --fail-on-warning
# Exits with code 2 if any failures, code 1 if warnings and --fail-on-warning