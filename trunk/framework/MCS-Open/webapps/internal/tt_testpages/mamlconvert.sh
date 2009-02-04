for i in *.jsp
do
  input_filename=$i

  input_filename_len=`expr length $input_filename`
  input_filename_len=`expr $input_filename_len - 4`
  filename=`expr substr $input_filename 1 $input_filename_len`
  output_filename="${filename}.xml"
  echo "Converting ${input_filename} to ${output_filename}"

  xml_callfilename="${filename}_xml.jsp" 
  sed 's/vt://g' $input_filename >xmlconvtemp1
  sed 's/<%@ include file=\"Volantis-mcs.jsp\" %>//' xmlconvtemp1 >xmlconvtemp2
  sed 's/styleClass/class/g' xmlconvtemp2 >xmlconvtemp3
  sed 's/accessKey/accesskey/g' xmlconvtemp3 >xmlconvtemp4
  sed 's/altText/alt/g' xmlconvtemp4 >xmlconvtemp5
  sed 's/httpEquiv/http-equiv/g' xmlconvtemp5 >xmlconvtemp6
  sed 's/colSpan/colspan/g' xmlconvtemp6 >xmlconvtemp7
  sed 's/rowSpan/rowspan/g' xmlconvtemp7 >xmlconvtemp8
  sed 's/noWrap/nowrap/g' xmlconvtemp8 >xmlconvtemp9
  sed 's/bgColor/bgcolor/g' xmlconvtemp9 >xmlconvtemp10

  echo '<?xml version="1.0" encoding="UTF-8" ?>' >xmlconvline1
  echo '<!DOCTYPE canvas>' >xmlconvline2
  cat xmlconvline1 xmlconvline2 xmlconvtemp10 > ${output_filename}  
  echo "s/xmlfile.xml/${output_filename}/g" >xmlconvsedscript
  sed -f xmlconvsedscript xmltemplate.jsp >$xml_callfilename 
  rm xmlconv*
done
