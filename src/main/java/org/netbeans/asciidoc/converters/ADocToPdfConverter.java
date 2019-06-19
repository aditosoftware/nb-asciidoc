package org.netbeans.asciidoc.converters;

import de.adito.aditoweb.nbm.nbide.nbaditointerface.conversions.IFileConverter;
import org.asciidoctor.*;
import org.jetbrains.annotations.*;
import org.netbeans.asciidoc.AsciidoctorConverter;
import org.openide.util.lookup.ServiceProvider;

import java.io.*;
import java.util.*;

/**
 * Converter for AsciiDoc to pdf files
 *
 * Possible parameters for the pParams map (For all options see {@link org.asciidoctor.Options}):
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
public class ADocToPdfConverter extends BaseAsciiDocConverter implements IFileConverter
{

  private static final String CONVERTER_FILE_TYPE = "pdf";

  private static List<String> asciiDocMimeTypes = List.of("text/asciidoc", "text/x-asciidoc");
  private static List<String> asciiDocFileEndings = List.of("adoc", "asciidoc");
  private static List<String> htmlMimeType = List.of("application/pdf", "application/x-pdf");
  private static List<String> pdfFileEndings = List.of(CONVERTER_FILE_TYPE);

  @Override
  public boolean canConvert(@NotNull String pSourceType, @NotNull String pTargetType, @NotNull Map<Object, Object> pParams)
  {
    return (asciiDocFileEndings.contains(pSourceType) || asciiDocMimeTypes.contains(pSourceType))
        && (pdfFileEndings.contains(pTargetType) || htmlMimeType.contains(pTargetType));
  }

  @Override
  public Object convert(@NotNull File pSourceLocation, @NotNull File pTargetLocation, @NotNull String pSourceType, @NotNull String pTargetType, @NotNull Map<Object, Object> pParams)
  {
    if(canConvert(pSourceType, pTargetType, Map.of()))
    {
      Options options = OptionsBuilder.options()
          .backend(CONVERTER_FILE_TYPE)
          .mkDirs(true)
          .toFile(adjustFileEnding(pTargetLocation, CONVERTER_FILE_TYPE)).get();
      AsciidoctorConverter.getDefault().getDoctor().convertFile(pSourceLocation, fillOptions(options, pParams));
    }
    return null;
  }

  @Nullable
  @Override
  public Object convert(@NotNull Reader pSource, @NotNull Writer pTarget, @NotNull String pSourceType, @NotNull String pTargetType, @NotNull Map<Object, Object> pParams) throws IOException
  {
    if(canConvert(pSourceType, pTargetType, Map.of()))
    {
      Options options = OptionsBuilder.options()
          .backend(CONVERTER_FILE_TYPE)
          .get();
      AsciidoctorConverter.getDefault().getDoctor().convert(pSource, pTarget, fillOptions(options, pParams));
    }
    return null;
  }
}
