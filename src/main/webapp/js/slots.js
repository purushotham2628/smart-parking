// Handles slot grid selection and submits booking form.
document.addEventListener('DOMContentLoaded', function () {
    const slots = document.querySelectorAll('.slot.available');
    const slotInput = document.getElementById('slotIdInput');
    const slotLabel = document.getElementById('selectedSlotLabel');
    const bookBtn   = document.getElementById('bookBtn');

    slots.forEach(s => {
        s.addEventListener('click', function () {
            slots.forEach(x => x.classList.remove('selected'));
            this.classList.add('selected');
            if (slotInput) slotInput.value = this.dataset.slotId;
            if (slotLabel) slotLabel.textContent =
                'Selected: ' + this.dataset.slotNumber + ' (' + this.dataset.vehicleType + ')';
            if (bookBtn)   bookBtn.disabled = false;
        });
    });
});
