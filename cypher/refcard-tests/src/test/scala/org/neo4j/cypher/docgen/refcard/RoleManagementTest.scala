/*
 * Copyright (c) 2002-2019 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.docgen.refcard

class RoleManagementTest extends AdministrationCommandTestBase {
  val title = "(★) ROLE MANAGEMENT"
  override val linkId = "administration/security/users-and-roles/#administration-security-roles"

  private def setup() = graph.withTx { tx =>
    tx.execute("CREATE USER alice SET PASSWORD 'secret' CHANGE NOT REQUIRED")
  }

  def text: String = {
    setup()
    """
###assertion=update-one
//

CREATE ROLE my_role
###

Create a role named `my_role`.

###assertion=update-one
//

CREATE ROLE my_second_role IF NOT EXISTS AS COPY OF my_role
###

Create a role named `my_second_role` unless it already exists, as a copy of the existing role `my_role`.

###assertion=update-two
//

GRANT ROLE my_role, my_second_role TO alice
###

Assign the roles `my_role` and `my_second_role` to the user `alice`.

###assertion=update-one
//

REVOKE ROLE my_second_role FROM alice
###

Remove the role `my_second_role` from the user `alice`.

###assertion=show
//

SHOW POPULATED ROLES WITH USERS
###

List all roles, and their users, that are assigned to users in the system.

###assertion=update-one
//

DROP ROLE my_role
###

Delete the role `my_role`.

"""
  }
}
