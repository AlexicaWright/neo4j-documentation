/*
 * Copyright (c) 2002-2018 "Neo4j,"
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
package org.neo4j.cypher.internal

import org.neo4j.cypher.internal.runtime.QueryContext
import org.neo4j.cypher.internal.runtime.interpreted.IsList
import org.neo4j.cypher.internal.runtime.interpreted.commands.values.KeyToken
import org.neo4j.graphdb.{Node, PropertyContainer, Relationship}

import scala.collection.Map

trait CypherSerializer {

  import scala.collection.JavaConverters._
  protected def serialize(a: Any, qtx: QueryContext): String = a match {
    case x: Node         => x.toString + serializeProperties(x, qtx)
    case x: Relationship => ":" + x.getType.name() + "[" + x.getId + "]" + serializeProperties(x, qtx)
    case x: Any if x.isInstanceOf[Map[_, _]] => makeString(x.asInstanceOf[Map[String, Any]], qtx)
    case x: Any if x.isInstanceOf[java.util.Map[_, _]] => makeString(x.asInstanceOf[java.util.Map[String, Any]].asScala, qtx)
    case IsList(coll)    => coll.asArray().map(elem => serialize(elem, qtx)).mkString("[", ",", "]")
    case x: String       => "\"" + x + "\""
    case v: KeyToken     => v.name
    case Some(x)         => x.toString
    case null            => "<null>"
    case x               => x.toString
  }

  protected def serializeProperties(x: PropertyContainer, qtx: QueryContext): String = {
    val (ops, id, deleted) = x match {
      case n: Node => (qtx.nodeOps, n.getId, qtx.nodeOps.isDeletedInThisTx(n.getId))
      case r: Relationship => (qtx.relationshipOps, r.getId, qtx.relationshipOps.isDeletedInThisTx(r.getId))
    }

    val keyValStrings = if (deleted) Array("deleted")
    else ops.propertyKeyIds(id).
      map(pkId => qtx.getPropertyKeyName(pkId) + ":" + serialize(ops.getProperty(id, pkId).asObject(), qtx))

    keyValStrings.mkString("{", ",", "}")
  }

  private def makeString(m: Map[String, Any], qtx: QueryContext) = m.map {
    case (k, v) => k + " -> " + serialize(v, qtx)
  }.mkString("{", ", ", "}")
}
