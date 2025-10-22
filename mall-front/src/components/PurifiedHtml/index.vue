<template>
  <div ref="htmlContainer"></div>
</template>

<script setup lang="ts">
import DOMPurify from "dompurify";
import { ref, onMounted, watch } from "vue";

const props = defineProps<{
  content: string;
}>();

const htmlContainer = ref<HTMLElement | null>(null);

function updateContent() {
  if (htmlContainer.value) {
    htmlContainer.value.innerHTML = DOMPurify.sanitize(props.content);
  }
}

watch(
  () => props.content,
  () => {
    updateContent();
  }
);

onMounted(() => {
  updateContent();
});
</script>
