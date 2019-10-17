package org.neo4j.cypher.docgen

import org.neo4j.cypher.docgen.tooling._
import org.neo4j.graphdb.Label
import org.neo4j.kernel.api.KernelTransaction.Type
import org.neo4j.kernel.api.security.AnonymousContext

import scala.collection.JavaConverters._

class SecurityAdministrationTest extends DocumentingTest with QueryStatisticsTestSupport {
  override def outputPath = "target/docs/dev/ql/administration/security/"

  override def doc: Document = new DocBuilder {
    doc("Security of Administration", "administration-security-administration")
    database("system")
    initQueries(
      "CREATE USER jake SET PASSWORD 'abc123' CHANGE NOT REQUIRED SET STATUS ACTIVE",
      "CREATE ROLE regularUsers",
      "CREATE ROLE noAccessUsers",
      "GRANT ROLE regularUsers TO jake",
      "DENY ACCESS ON DATABASE neo4j TO noAccessUsers"
    )
    synopsis("This section explains how to use Cypher to manage Neo4j administrative privileges.")
    p(
      """All of the commands described in the enclosing <<administration, Administration>> section require that the user executing the commands has the rights to do so.
        |These privileges can be conferred either by granting the user the `admin` role, which enables all administrative rights, or by granting specific combinations of privileges..
        |""".stripMargin)
    p(
      """
        |* <<administration-security-administration-introduction, The 'admin' role>>
        |* <<administration-security-administration-database-privileges, Database administration>>
        |** <<administration-security-administration-database-access, The database ACCESS privilege>>
        |** <<administration-security-administration-database-startstop, The database START/STOP privileges>>
        |** <<administration-security-administration-database-indexes, The INDEX MANAGEMENT privileges>>
        |** <<administration-security-administration-database-constraints, The CONSTRAINT MANAGEMENT privileges>>
        |** <<administration-security-administration-database-tokens, The NAME MANAGEMENT privileges>>
        |** <<administration-security-administration-database-all, Granting all database administration privileges>>
        |* <<administration-security-administration-dbms-privileges, DBMS administration>>
        |""".stripMargin)
    section("The 'admin' role", "administration-security-administration-introduction") {
      p("include::admin-role-introduction.asciidoc[]")
    }
    section("Database administration", "administration-security-administration-database-privileges") {
      synopsis("This section explains how to use Cypher to manage privileges for Neo4j database administrative rights.")
      p("include::database/admin-role-database.asciidoc[]")
      p("include::database/admin-database-syntax.asciidoc[]")
      p("image::grant-privileges-database.png[title=\"Syntax of GRANT and DENY Database Privileges\"]")
      section("The database ACCESS privilege", "administration-security-administration-database-access") {
        p(
          """The `ACCESS` privilege can be used to enable the ability to access a database.
            |If this is not granted to users, they will not even be able to start transactions on the relevant database.""".stripMargin)
        p("include::database/grant-database-access-syntax.asciidoc[]")

        p(
          """For example, granting the ability to access the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT ACCESS ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p("The `ACCESS` privilege can also be denied.")
        p("include::database/deny-database-access-syntax.asciidoc[]")

        p("For example, denying the ability to access to the database `neo4j` to the role `regularUsers` is done using the following query.")
        query("DENY ACCESS ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p("The privileges granted can be seen using the `SHOW PRIVILEGES` command:")
        query("SHOW ROLE regularUsers PRIVILEGES", assertPrivilegeShown(Seq(
          Map("grant" -> "GRANTED", "action" -> "access"),
          Map("grant" -> "DENIED", "action" -> "access")
        ))) {
          resultTable()
        }
      }
      section("The database START/STOP privileges", "administration-security-administration-database-startstop") {
        p(
          """The `START` privilege can be used to enable the ability to start a database.""".stripMargin)
        p("include::database/grant-database-start-syntax.asciidoc[]")

        p(
          """For example, granting the ability to start the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT START ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p("The `START` privilege can also be denied.")
        p("include::database/deny-database-start-syntax.asciidoc[]")

        p("For example, denying the ability to start to the database `neo4j` to the role `regularUsers` is done using the following query.")
        query("DENY START ON DATABASE system TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p(
          """The `STOP` privilege can be used to enable the ability to stop a database.""".stripMargin)
        p("include::database/grant-database-stop-syntax.asciidoc[]")

        p(
          """For example, granting the ability to stop the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT STOP ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p("The `STOP` privilege can also be denied.")
        p("include::database/deny-database-stop-syntax.asciidoc[]")

        p("For example, denying the ability to stop to the database `neo4j` to the role `regularUsers` is done using the following query.")
        query("DENY STOP ON DATABASE system TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p("The privileges granted can be seen using the `SHOW PRIVILEGES` command:")
        query("SHOW ROLE regularUsers PRIVILEGES", assertPrivilegeShown(Seq(
          Map("grant" -> "GRANTED", "action" -> "access"),
          Map("grant" -> "DENIED", "action" -> "access"),
          Map("grant" -> "GRANTED", "action" -> "start_database"),
          Map("grant" -> "DENIED", "action" -> "start_database"),
          Map("grant" -> "GRANTED", "action" -> "stop_database"),
          Map("grant" -> "DENIED", "action" -> "stop_database")
        ))) {
          resultTable()
        }
      }
      section("The INDEX MANAGEMENT privileges", "administration-security-administration-database-indexes") {
        p(
          """Indexes can be created or deleted with the `CREATE INDEX` and `DROP INDEX` commands.
            |The privilege to do this can be granted with `GRANT CREATE INDEX` and `GRANT DROP INDEX` commands.""".stripMargin)
        p("include::database/index-management-syntax.asciidoc[]")

        p(
          """For example, granting the ability to create indexes on the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT CREATE INDEX ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
      }
      section("The CONSTRAINT MANAGEMENT privileges", "administration-security-administration-database-constraints") {
        p(
          """Constraints can be created or deleted with the `CREATE CONSTRAINT` and `DROP CONSTRAINT` commands.
            |The privilege to do this can be granted with `GRANT CREATE CONSTRAINT` and `GRANT DROP CONSTRAINT` commands.""".stripMargin)
        p("include::database/constraint-management-syntax.asciidoc[]")

        p(
          """For example, granting the ability to create constraints on the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT CREATE CONSTRAINT ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
      }
      section("The NAME MANAGEMENT privileges", "administration-security-administration-database-tokens") {
        p(
          """The right to create new labels, relationship types or propery names is different from the right to create nodes, relationships or properties.
            |The latter is managed using database `WRITE` privileges, while the former is managed using specific `GRANT/DENY CREATE NEW ...` commands for each type.""".stripMargin)
        p("include::database/name-management-syntax.asciidoc[]")

        p(
          """For example, granting the ability to create new properties on nodes or relationships in the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT CREATE NEW PROPERTY NAME ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
      }
      section("Granting all database administration privileges", "administration-security-administration-database-all") {
        p(
          """Conferring the right to perform all of the above tasks can be achieved with a single command:""".stripMargin)
        p("include::database/all-management-syntax.asciidoc[]")

        p(
          """For example, granting the ability to create indexes, constraints, labels, relationship types and property names on the database `neo4j` to the role `regularUsers` is done using the following query.""".stripMargin)
        query("GRANT ALL DATABASE PRIVILEGES ON DATABASE neo4j TO regularUsers", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 4)
        })) {
          statsOnlyResultTable()
        }

        p("The privileges granted can be seen using the `SHOW PRIVILEGES` command:")
        query("SHOW ROLE regularUsers PRIVILEGES", assertPrivilegeShown(Seq(
          Map("grant" -> "GRANTED", "action" -> "access", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "start_database", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "stop_database", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "create_index", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "drop_index", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "create_constraint", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "drop_constraint", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "create_label", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "create_reltype", "role" -> "regularUsers"),
          Map("grant" -> "GRANTED", "action" -> "create_propertykey", "role" -> "regularUsers")
        ))) {
          resultTable()
        }
      }
    }
    section("DBMS administration", "administration-security-administration-dbms-privileges") {
      p("include::dbms/admin-role-dbms.asciidoc[]")
      section("Using a custom role to manage DBMS privileges", "administration-security-administration-dbms-custom") {
        p("include::dbms/admin-role-dbms-custom.asciidoc[]")
        p("First we copy the 'admin' role:")/*
        //TODO: Fix system graph initialization in Neo4j 4.0 to initialize the security model earlier
        query("CREATE ROLE usermanager AS COPY OF admin", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 10)
        })) {
          statsOnlyResultTable()
        }
        p("Then we DENY ACCESS to normal databases:")
        query("DENY ACCESS ON DATABASE * TO usermanager", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
        p("And DENY START and STOP for normal databases:")
        query("DENY START ON DATABASE * TO usermanager", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
        query("DENY STOP ON DATABASE * TO usermanager", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
        p("And DENY index and constraint management:")
        query("DENY INDEX MANAGEMENT ON DATABASE * TO usermanager", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
        query("DENY CONSTRAINT MANAGEMENT ON DATABASE * TO usermanager", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }
        p("And finally DENY label, relationship type and property name:")
        query("DENY NAME MANAGEMENT ON DATABASE * TO usermanager", ResultAssertions((r) => {
          assertStats(r, systemUpdates = 1)
        })) {
          statsOnlyResultTable()
        }

        p("The resulting role should have privileges that only allow the DBMS capabilities, like user and role management:")
        query("SHOW ROLE usermanager PRIVILEGES", assertPrivilegeShown(Seq(Map()))) {
          p("Lists all privileges for role 'usermanager'")
          resultTable()
        }*/
      }
    }
  }.build()

  private def assertPrivilegeShown(expected: Seq[Map[String, AnyRef]]) = ResultAndDbAssertions((p, db) => {
    println(p.resultAsString)
    println(s"Searching for $expected")
    val found = p.toList.filter { row =>
      println(s"Checking row: $row")
      val m = expected.filter { expectedRow =>
        expectedRow.forall {
          case (k, v) => row.contains(k) && row(k) == v
        }
      }
      println(s"\tmatched: $m")
      m.nonEmpty
    }
    found.nonEmpty
  })
}
