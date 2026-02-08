# 🔐 SSL Checker CLI

[![Java Support](https://img.shields.io/badge/Java-17%2B-blue?logo=java)](https://jdk.java.net/)
[![Built with Maven](https://img.shields.io/badge/Built%20with-Maven-C71A36?logo=apache-maven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

A modern, fast, and feature-rich Java CLI tool designed to help developers and DevOps engineers monitor SSL/TLS certificates. Never let a certificate expire unexpectedly again.

---

## 🚀 Key Features

- **Multi-Domain Support**: Check individual domains or bulk lists from files.
- **Expiration Alerts**: Customizable warning thresholds for imminent certificate expirations.
- **CI/CD Ready**: Output results in clean JSON format for easy integration with automation tools.
- **Native Performance**: Supports GraalVM native images for instant startup and low memory footprint.
- **Detailed Analytics**: Displays Subject, Issuer, Validity dates, Protocol, and SANs.

---

## 🛠️ Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven** (for building from source)

### Installation & Build
```bash
# Clone the repository
git clone https://github.com/jake/ssl-checker.git
cd ssl-checker

# Build the project
mvn clean package
```

---

## 📖 Usage

### Single Domain Check
Get a detailed report for a single domain:
```bash
java -jar target/ssl-checker-1.0.0.jar check google.com
```

### Bulk Check from File
Scan multiple domains at once using a text file:
```bash
java -jar target/ssl-checker-1.0.0.jar bulk -f domains.txt
```

### JSON Output
Perfect for automated monitoring and reporting:
```bash
java -jar target/ssl-checker-1.0.0.jar check google.com --json
```

---

## 💻 Examples

### Terminal Output
```text
✓ google.com:443
  Subject:    CN=*.google.com
  Issuer:     CN=GTS CA 1C3, O=Google Trust Services LLC, C=US
  Valid:      2024-01-15 to 2024-04-15
  Expires in: 45 days
  Protocol:   TLSv1.3
  SANs:       *.google.com, *.appengine.google.com, *.bdn.dev, ...
```

### JSON Format
```json
{
  "host": "expired.badssl.com",
  "port": 443,
  "error": "Certificate expired 365 days ago",
  "checkedAt": "2024-02-09T10:30:00Z"
}
```

---

## 🚄 Native Image Support

Generate a standalone binary using GraalVM for maximum performance:

```bash
# Compile to native binary
mvn native:compile

# Run the native binary
./target/ssl-checker check google.com --warn 60
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.