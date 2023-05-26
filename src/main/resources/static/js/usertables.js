$(function() {
  $("#user-table").dataTable({
    // DataTablesを日本語化する
    language: {
      url: "/webjars/datatables-plugins/i18n/ja.json"
    },
    // 各種ボタンを有効化する
    dom: "Bfrtip",
    buttons: ["excelHtml5", "csvHtml5", "print"]
  });
});
