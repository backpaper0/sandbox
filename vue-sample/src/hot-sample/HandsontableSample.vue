<template>
  <div class="section">
    <HotTable :root="root" :settings="settings" ref="hotSample"></HotTable>
  </div>
</template>

<script>
import Handsontable from 'handsontable'
import HotTable from 'vue-handsontable-official'

let hotSample

export default {
  components: {
    HotTable
  },
  data() {
    return {
      root: 'hot-sample',
      settings: {
        data: Handsontable.helper.createSpreadsheetData(10, 10),
        columns: [
          {
            renderer(hotInstance, td, row, column, prop, value, cellProperties) {
              const html = '<button type="button" class="button"><span class="icon"><i class="fa fa-trash"></i></span></button>'
              Handsontable.renderers.HtmlRenderer.call(this, hotInstance, td, row, column, prop, html, cellProperties)
              const trashButton = td.getElementsByTagName('button')[0]
              trashButton.addEventListener('click', event => {
                if (hotSample) {
                  hotSample.alter('remove_row', row)
                }
              })
            },
            disableVisualSelection: true,
            readOnly: true
          }, {}, {}, {}, {},
          {}, {}, {}, {}, {}
        ]
      }
    }
  },
  mounted() {
    hotSample = this.$refs.hotSample.table
  }
}
</script>

<style lang="sass">
</style>
