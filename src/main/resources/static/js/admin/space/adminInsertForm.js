/* 이미지 미리보기 스크립트 */
document.getElementById('spImg').addEventListener('change', function(event) {
    var files = event.target.files;
    var preview = document.getElementById('imagePreview');
    preview.innerHTML = '';

    Array.from(files).forEach(function(file) {
        var reader = new FileReader();
        reader.onload = function(e) {
            var img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('preview-image');
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    });
});
