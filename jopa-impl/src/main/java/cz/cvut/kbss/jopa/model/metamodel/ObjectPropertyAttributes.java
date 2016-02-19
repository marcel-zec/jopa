/**
 * Copyright (C) 2011 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jopa.model.metamodel;

import cz.cvut.kbss.jopa.model.IRI;
import cz.cvut.kbss.jopa.model.MetamodelImpl;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;

import java.lang.reflect.Field;

class ObjectPropertyAttributes extends PropertyAttributes {

    @Override
    void resolve(Field field, MetamodelImpl metamodel, Class<?> fieldValueCls) {
        super.resolve(field, metamodel, fieldValueCls);
        final OWLObjectProperty oop = field.getAnnotation(OWLObjectProperty.class);
        assert oop != null;

        this.persistentAttributeType = Attribute.PersistentAttributeType.OBJECT;
        this.iri = IRI.create(oop.iri());
        this.cascadeTypes = oop.cascade();
        this.fetchType = oop.fetch();
        this.type = metamodel.entity(fieldValueCls);
    }
}