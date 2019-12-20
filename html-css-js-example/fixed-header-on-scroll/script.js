const header = document.querySelector("header");

const a = header.offsetTop;

function update() {
  if (document.documentElement.scrollTop < a) {
    header.classList.remove("fixed");
  } else {
    header.classList.add("fixed");
  }
}

update();

document.addEventListener("scroll", function() {
  update();
});
