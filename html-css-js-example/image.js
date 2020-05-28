
const file = document.querySelector('#file');
const preview = document.querySelector('#preview');

file.addEventListener('change', (changeEvent) => {
  while (preview.firstChild) {
    preview.removeChild(preview.firstChild);
  }
  const image = new Image();
  image.src = URL.createObjectURL(file.files[0]);
  image.onerror = (errorEvent) => {
    preview.removeChild(image);
    window.alert('Error');
  };
  preview.appendChild(image);
}, false);
