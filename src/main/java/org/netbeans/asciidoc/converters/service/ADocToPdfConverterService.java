package org.netbeans.asciidoc.converters.service;

import de.adito.aditoweb.nbm.nbide.nbaditointerface.conversions.IFileConverter;
import org.asciidoctor.Options;
import org.netbeans.asciidoc.converters.ADocToPdfConverter;
import org.openide.util.lookup.ServiceProvider;

/**
 * Wrapper for a service of the ADocToPdfConverter
 *
 * Possible parameters for the pParams map (For all options see {@link Options}):
 * "safe":
 * unsafe
 * safe
 * server
 * secure
 * "in_place":
 * true
 * false
 *
 * @author m.kaspera, 08.04.2019
 */
@ServiceProvider(service = IFileConverter.class)
public class ADocToPdfConverterService extends ADocToPdfConverter implements IFileConverter
{

}
