package org.neo4j.cypher.docgen

import org.neo4j.cypher.docgen.tooling._

class SecurityWritePrivilegesTest extends DocumentingTest with QueryStatisticsTestSupport {
  override def outputPath = "target/docs/dev/ql/administration/security/"

  override def doc: Document = new DocBuilder {
    doc("Write privileges", "administration-security-writes")
    database("system")
    initQueries(
      "CREATE USER jake SET PASSWORD 'abc123' CHANGE NOT REQUIRED SET STATUS ACTIVE",
      "CREATE ROLE regularUsers",
      "GRANT ROLE regularUsers TO jake",
      "GRANT ACCESS ON DATABASE neo4j TO regularUsers"
    )
    synopsis("This section explains how to use Cypher to manage write privileges for Neo4j.")

    p(
      """
        |* <<administration-security-writes-create, The `CREATE` privilege>>
        |* <<administration-security-writes-delete, The `DELETE` privilege>>
        |* <<administration-security-writes-set-label, The `SET LABEL` privilege>>
        |* <<administration-security-writes-remove-label, The `REMOVE LABEL` privilege>>
        |* <<administration-security-writes-set-property, The `SET PROPERTY` privilege>>
        |* <<administration-security-writes-merge, The `MERGE` privilege>>
        |* <<administration-security-writes-write, The `WRITE` privilege>>
        |* <<administration-security-writes-all, `ALL GRAPH PRIVILEGES`>>
        |""".stripMargin)

        p(
      """
        |There are several separate write privileges:
        |
        |* `WRITE` - this privilege can only be assigned to all nodes, relationships, and properties in the entire graph.
        |""".stripMargin)

    section("The `CREATE` privilege", "administration-security-writes-create", "enterprise-edition") {

    }

    section("The `DELETE` privilege", "administration-security-writes-delete", "enterprise-edition") {

    }

    section("The `SET LABEL` privilege", "administration-security-writes-set-label", "enterprise-edition") {

    }

    section("The `REMOVE LABEL` privilege", "administration-security-writes-remove-label", "enterprise-edition") {

    }

    section("The `SET PROPERTY` privilege", "administration-security-writes-set-property", "enterprise-edition") {

    }

    section("The `MERGE` privilege", "administration-security-writes-merge", "enterprise-edition") {

    }

    section("The `WRITE` privilege", "administration-security-writes-write", "enterprise-edition") {
      p(
        """The `WRITE` privilege enables you to write on a graph.""".stripMargin)
      p("include::grant-write-syntax.asciidoc[]")

      p(
        """For example, granting the ability to write on the graph `neo4j` to the role `regularUsers` would be achieved using:""".stripMargin)
      query("GRANT WRITE ON GRAPH neo4j TO regularUsers", ResultAssertions(r => {
        assertStats(r, systemUpdates = 2)
      })) {
        statsOnlyResultTable()
      }
      note {
        p("Unlike with `GRANT READ` it is not possible to restrict `WRITE` privileges to specific ELEMENTS, NODES or RELATIONSHIPS.")
      }

      p("The `WRITE` privilege can also be denied.")
      p("include::deny-write-syntax.asciidoc[]")

      p("For example, denying the ability to write on the graph `neo4j` to the role `regularUsers` would be achieved using:")
      query("DENY WRITE ON GRAPH neo4j TO regularUsers", ResultAssertions(r => {
        assertStats(r, systemUpdates = 2)
      })) {
        statsOnlyResultTable()
      }
      note {
        p(
          """Users with `WRITE` privilege but restricted `TRAVERSE` privileges will not be able to do `DETACH DELETE` in all cases.
            | See <<operations-manual#detach-delete-restricted-user, Operations Manual -> Fine-grained access control>> for more info.""".stripMargin)
      }
    }

    section("`ALL GRAPH PRIVILEGES`", "administration-security-writes-all", "enterprise-edition") {

    }
  }.build()

}
