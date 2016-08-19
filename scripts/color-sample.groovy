/*
 * インデックスカラーの色見本を作る。
 * 作業ディレクトリにcolor-sample.xlsが出力される。
 * 
 * 本当はxlsxで出力したかったけどなんかClassNotFoundExceptionになった。
 * Javaなら出来るのに。。。
 * 面倒なのでxls出力で妥協しておく。
 * 
 */
//@Grab('org.apache.poi:poi-ooxml:3.14')
@Grab('org.apache.poi:poi:3.14')
import org.apache.poi.ss.usermodel.*
import org.apache.poi.hssf.usermodel.*

def workbook = new HSSFWorkbook()

def sheet = workbook.createSheet()

IndexedColors.values().eachWithIndex { ic, rowIndex ->
    def row = sheet.createRow(rowIndex)
    def cs = workbook.createCellStyle()
    cs.fillPattern = CellStyle.SOLID_FOREGROUND
    cs.fillForegroundColor = ic.index
    row.createCell(0).cellStyle = cs
    row.createCell(1).cellValue = ic.name()
}

new File("color-sample.xls").withOutputStream { out ->
    workbook.write(out)
}

workbook.close()

