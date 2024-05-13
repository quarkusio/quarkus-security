Quarkus Security API
====================

[![Version](https://img.shields.io/maven-central/v/io.quarkus.security/quarkus-security-api?logo=apache&style=for-the-badge)](https://search.maven.org/artifact/io.quarkus.security/quarkus-security-api)

The Quarkus core security API.

## Release

```bash
# Bump version and create the tag
mvn release:prepare -Prelease
# Build the tag and push to OSSRH
mvn release:perform -Prelease
```

The staging repository is automatically closed. The sync with Maven Central should take ~30 minutes.
