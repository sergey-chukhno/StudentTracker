document.addEventListener('DOMContentLoaded', () => {
  const slides = Array.from(document.querySelectorAll('.slide'));
  let currentSlide = 0;

  function showSlide(idx) {
    slides.forEach((slide, i) => {
      slide.classList.toggle('active', i === idx);
    });
    currentSlide = idx;
  }

  // Initial fade in for title slide
  const title = document.querySelector('.slide-title .main-title');
  const image = document.querySelector('.slide-title .main-image');
  title.style.opacity = 0;
  image.style.opacity = 0;
  setTimeout(() => {
    title.style.transition = 'opacity 0.8s';
    title.style.opacity = 1;
    setTimeout(() => {
      image.style.transition = 'opacity 0.8s';
      image.style.opacity = 1;
    }, 500);
  }, 200);

  // Professional hover effect for image
  image.addEventListener('mouseenter', () => {
    image.classList.add('hovered');
  });
  image.addEventListener('mouseleave', () => {
    image.classList.remove('hovered');
  });

  // Scroll button listeners
  const leftBtn = document.querySelector('.scroll-btn.left');
  const rightBtn = document.querySelector('.scroll-btn.right');
  leftBtn.addEventListener('click', () => {
    if (currentSlide > 0) {
      showSlide(currentSlide - 1);
    }
  });
  rightBtn.addEventListener('click', () => {
    if (currentSlide < slides.length - 1) {
      showSlide(currentSlide + 1);
    }
  });

  // Show initial slide
  showSlide(0);
}); 